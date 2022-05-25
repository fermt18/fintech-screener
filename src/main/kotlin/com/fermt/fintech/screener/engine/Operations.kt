package com.fermt.fintech.screener.engine

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.IntStream
import kotlin.math.pow

class Operations {

    fun calcEV(marketCap: Long, totalDebt: Long, cashAndCashEq: Long): Long {
        return 1L
    }

    // DCF -------------------
    fun calcDFCFF(ebitAfterTax: Double,
                  growthRate: Double,
                  reinvestmentRate: Double,
                  discountRate: Double,
                  years: Int,
                  cf: Double,
                  growthRateTerminal: Double,
                  reinvestmentRateTerminal: Double,
                  discountRateTerminal: Double): Double {
        val dcfPeriod = dfcff(ebitAfterTax, growthRate, reinvestmentRate, discountRate, years)
        val dcfTerminal = dfcffAtTerminal(cf, growthRateTerminal, reinvestmentRateTerminal, discountRate, discountRateTerminal, years)
        return dcfPeriod + dcfTerminal
    }

    fun dcfAtYear(cashFlow: Double, discountRate: Double, year: Int): Double {
        return cashFlow / (1 + discountRate).pow(year)
    }

    fun dfcffAtYear(cf: Double, growthRate: Double, reinvestmentRate: Double, discountRate: Double, year: Int): Double {
        val cfGrown = cf * (1 + growthRate).pow(year)
        val cfReinvested = cfGrown * (1 - reinvestmentRate)
        return dcfAtYear(cfReinvested, discountRate, year)
    }

    fun dfcffAtTerminal(cf: Double, growthRateTerminal: Double, reinvestmentRateTerminal: Double, discountRate: Double, discountRateTerminal: Double, year: Int): Double {
        val cfGrown = cf * (1 + growthRateTerminal)
        val cfReinvested = cfGrown * (1 - reinvestmentRateTerminal)
        val terminalValue = terminalValue(cfReinvested, growthRateTerminal, discountRateTerminal)
        return roundToNDecimals(dcfAtYear(terminalValue, discountRate, year))
    }

    fun dfcff(ebitAfterTax: Double, growthRate: Double, reinvestmentRate: Double, discountRate: Double, years: Int): Double {
        val dcfList = mutableListOf<Double>()
        for(i in IntStream.range(1, years + 1)) {
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