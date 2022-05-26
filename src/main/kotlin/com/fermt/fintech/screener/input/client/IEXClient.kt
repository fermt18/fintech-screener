package com.fermt.fintech.screener.input.client

import com.fermt.fintech.screener.engine.Operations
import kotlin.math.pow

class IEXClient: ClientInt {
    override fun getSector(ticker: String): String {
        return "Digital Ads"
    }

    override fun getEBIT(ticker: String): Long {
        return 100L
    }

    override fun getNetIncome(ticker: String): Long {
        return 10L
    }

    override fun getIncomeTax(ticker: String): Long {
        return 35L
    }

    override fun getSharesOutstanding(ticker: String): Long {
        return 3500000L
    }

    override fun getPrice(ticker: String): Double {
        return 5.27
    }

    override fun getTotalDebt(ticker: String): Long {
        return 27L
    }

    override fun getCashAndCashEq(ticker: String): Long {
        return 3L
    }

    override fun getCurrentAssets(ticker: String): Long {
        return 185L
    }

    override fun getTotalAssets(ticker: String): Long {
        return 1850L
    }

    override fun getCurrentLiabilities(ticker: String): Long {
        return 15L
    }

    override fun getTotalEquity(ticker: String): Long {
        return 5L
    }

    override fun getOpCashFlow(ticker: String): Long {
        return 8L
    }

    override fun getCapEx(ticker: String): Long {
        return 2L
    }

}