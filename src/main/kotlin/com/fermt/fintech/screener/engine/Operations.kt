package com.fermt.fintech.screener.engine

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.IntStream
import kotlin.math.pow

class Operations {

    companion object {
        fun calcMarketCap(price: Double, sharesOutstanding: Long): Long {
            return (price * sharesOutstanding).toLong()
        }

        fun calcEV(marketCap: Long, totalDebt: Long, cashAndCashEq: Long): Long {
            return marketCap + totalDebt - cashAndCashEq
        }

        fun calcFCF(opCashFlow: Long, capEx: Long): Long {
            return opCashFlow - capEx
        }

        fun calcCurrentRatio(currentAssets: Long, currentLiabilities: Long): Double {
            if(currentLiabilities == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(currentAssets.toDouble() / currentLiabilities.toDouble())
        }

        fun calcDebtToEquity(totalDebt: Long, totalEquity: Long): Double {
            if(totalEquity == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(totalDebt.toDouble() / totalEquity.toDouble())
        }

        fun calcRoA(netIncome: Long, totalAssets: Long): Double {
            if(totalAssets == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(netIncome.toDouble() / totalAssets.toDouble())
        }

        fun calcRoE(netIncome: Long, totalEquity: Long): Double {
            if(totalEquity == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(netIncome.toDouble() / totalEquity.toDouble())
        }

        fun calcRoIC(ebit: Long, totalAssets: Long, currentLiabilities: Long): Double {
            if(ebit == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(ebit.toDouble() / (totalAssets.toDouble() + currentLiabilities.toDouble()))
        }

        fun calcPE(price: Double, netIncome: Long): Double {
            if(netIncome == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(price / netIncome.toDouble())
        }

        fun calcEVEBIT(ev: Long, ebit: Long): Double {
            if(ebit == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(ev.toDouble() / ebit.toDouble())
        }

        fun calcEVFCFF(ev: Long, fcff: Long): Double {
            if(fcff == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(ev.toDouble() / fcff.toDouble())
        }

        fun calcTaxRate(netIncome: Long, incomeTax: Long): Double {
            if(netIncome == 0L) return Double.POSITIVE_INFINITY
            return roundToNDecimals(incomeTax.toDouble() / netIncome.toDouble())
        }

        fun calcNOPAT(ebit: Long, taxRate: Double): Long {
            return (ebit.toDouble() * (1 - taxRate)).toLong()
        }

        // DCF -------------------
        fun calcDFCFF(
            ebitAfterTax: Long,
            growthRate: Double,
            reinvestmentRate: Double,
            discountRate: Double,
            years: Int,
            cf: Double,
            growthRateTerminal: Double,
            reinvestmentRateTerminal: Double,
            discountRateTerminal: Double
        ): Double {
            val dcfPeriod = dfcff(ebitAfterTax, growthRate, reinvestmentRate, discountRate, years)
            val dcfTerminal = dfcffAtTerminal(
                cf,
                growthRateTerminal,
                reinvestmentRateTerminal,
                discountRate,
                discountRateTerminal,
                years
            )
            return roundToNDecimals(dcfPeriod + dcfTerminal)
        }

        fun dcfAtYear(cashFlow: Double, discountRate: Double, year: Int): Double {
            return cashFlow / (1 + discountRate).pow(year)
        }

        fun dfcffAtYear(
            cf: Long,
            growthRate: Double,
            reinvestmentRate: Double,
            discountRate: Double,
            year: Int
        ): Double {
            val cfGrown = cf * (1 + growthRate).pow(year)
            val cfReinvested = cfGrown * (1 - reinvestmentRate)
            return dcfAtYear(cfReinvested, discountRate, year)
        }

        fun dfcffAtTerminal(
            cf: Double,
            growthRateTerminal: Double,
            reinvestmentRateTerminal: Double,
            discountRate: Double,
            discountRateTerminal: Double,
            year: Int
        ): Double {
            val cfGrown = cf * (1 + growthRateTerminal)
            val cfReinvested = cfGrown * (1 - reinvestmentRateTerminal)
            val terminalValue = terminalValue(cfReinvested, growthRateTerminal, discountRateTerminal)
            return roundToNDecimals(dcfAtYear(terminalValue, discountRate, year))
        }

        fun dfcff(
            ebitAfterTax: Long,
            growthRate: Double,
            reinvestmentRate: Double,
            discountRate: Double,
            years: Int
        ): Double {
            val dcfList = mutableListOf<Double>()
            for (i in IntStream.range(1, years + 1)) {
                dcfList.add(dfcffAtYear(ebitAfterTax, growthRate, reinvestmentRate, discountRate, i))
            }
            return roundToNDecimals(dcfList.sum())
        }

        fun terminalValue(cashFlow: Double, growthRateTerminal: Double, discountRateTerminal: Double): Double {
            return roundToNDecimals(cashFlow / (discountRateTerminal - growthRateTerminal))
        }
        // -----------------------------------------------------------------------------

        private fun roundToNDecimals(v: Double): Double {
            val bd = BigDecimal(v)
            return bd.setScale(2, RoundingMode.HALF_UP).toDouble()
        }
    }

}