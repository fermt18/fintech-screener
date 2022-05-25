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
    fun calcDFCFF(): Double {
        val ebitAfterTax = 10.0
        val growthRate = 0.15
        val reinvestmentRate = 0.5
        val discountRate = 0.09
        val years = 10
        val dcfPeriod = dfcff(ebitAfterTax, growthRate, reinvestmentRate, discountRate, years)

        val cf = ebitAfterTax * (1 + growthRate).pow(years)
        val growthRateTerminal = 0.02
        val reinvestmentRateTerminal = 0.2
        val discountRateTerminal = 0.3
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