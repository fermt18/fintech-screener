package com.fermt.fintech.screener.engine

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.pow

class TestDCF {

    // DCF: how much is worth now an investment with a given growthRate having into account a particular discountRate?
    // FCFF = NOPAT - ReinvestmentRate
    // reinvestmentRate: amount of retained earnings needed to keep the business running / growing
    // growthRate: Earnings growing rate
    // discountRate: Expected rate of return due to inflation and cost of opportunity, typically = wacc
    // At bare minimum discountRate = inflationRate and growthRate = discountRate

    val dcf = Operations()

    @Test
    fun test_dcf_at_year(){
        val cashFlow = 1.0
        val growthRate = 0.08
        val discountRate = 0.08
        val year = 1
        Assertions.assertEquals(cashFlow, dcf.dcfAtYear(cashFlow * (1 + growthRate), discountRate, year))
    }

    @Test
    fun test_dfcff_at_year(){
        val cashFlow = 1.0
        val growthRate = 0.08
        val reinvestmentRate = 0.3
        val discountRate = 0.08
        val years = 1
        val fcffValue = dcf.dfcffAtYear(cashFlow, growthRate, reinvestmentRate, discountRate, years)
        Assertions.assertEquals(cashFlow * (1 - reinvestmentRate), fcffValue)
    }

    @Test
    fun test_dfcff() {
        val cashFlow = 3586.20
        val growthRate = 0.075
        val reinvestmentRate = 0.32
        val discountRate = 0.087
        val years = 5
        Assertions.assertEquals(
            11795.16,
            dcf.dfcff(
                cashFlow,
                growthRate,
                reinvestmentRate,
                discountRate,
                years
            )
        )
    }

    @Test
    fun test_terminalValue() {
        val cashFlow = 2470.87
        val growthRateTerminal = 0.0379
        val discountRateTerminal = 0.0705
        Assertions.assertEquals(75793.56, dcf.terminalValue(cashFlow, growthRateTerminal, discountRateTerminal))
    }

    @Test
    fun test_enterpriseIntrinsicValue() {
        val cashFlow = 3586.20
        val growthRate = 0.075
        val reinvestmentRate = 0.32
        val discountRate = 0.087
        val years = 5
        val growthRateTerminal = 0.0379
        val reinvestmentRateTerminal = 0.5376
        val discountRateTerminal = 0.0705
        Assertions.assertEquals(
            11795.16, dcf.dfcff(
                cashFlow,
                growthRate,
                reinvestmentRate,
                discountRate,
                years
            )
        )
        Assertions.assertEquals(
            49944.18, dcf.dfcffAtTerminal(
                cashFlow * (1 + growthRate).pow(years),
                growthRateTerminal,
                reinvestmentRateTerminal,
                discountRate,
                discountRateTerminal,
                years
            )
        )
    }
}