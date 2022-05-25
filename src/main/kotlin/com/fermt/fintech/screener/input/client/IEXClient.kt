package com.fermt.fintech.screener.input.client

import com.fermt.fintech.screener.engine.Operations
import kotlin.math.pow

class IEXClient: ClientInt {

    override fun getSector(ticker: String): String {
        return "Digital Ads"
    }

    override fun getMarketCap(ticker: String): Long {
        return 1L
    }

    override fun getEV(ticker: String): Long {
        return 1L
    }

    override fun getCurrentRatio(ticker: String): Double {
        return 1.0
    }

    override fun getDebtToEquity(ticker: String): Double {
        return 1.0
    }

    override fun getRoA(ticker: String): Double {
        return 1.0
    }

    override fun getRoE(ticker: String): Double {
        return 1.0
    }

    override fun getRoIC(ticker: String): Double {
        return 1.0
    }

    override fun getPE(ticker: String): Double {
        return 1.0
    }

    override fun getEVEBIT(ticker: String): Double {
        return 1.0
    }

    override fun getEVFCFF(ticker: String): Double {
        return 1.0
    }

    override fun getIV(ticker: String): Double {
        val ebitAfterTax = 10.0
        val growthRate = 0.15
        val reinvestmentRate = 0.5
        val discountRate = 0.09
        val years = 10
        val cf = ebitAfterTax * (1 + growthRate).pow(years)
        val growthRateTerminal = 0.02
        val reinvestmentRateTerminal = 0.2
        val discountRateTerminal = 0.3
        return Operations().calcDFCFF(
            ebitAfterTax, growthRate, reinvestmentRate, discountRate, years,
            cf, growthRateTerminal, reinvestmentRateTerminal, discountRateTerminal)
    }

    private fun getEBIT(ticker: String, period: String): Double {
        return 15.0
    }

    private fun getTaxRate(ticker: String, period: String): Double {
        return 0.35
    }
}