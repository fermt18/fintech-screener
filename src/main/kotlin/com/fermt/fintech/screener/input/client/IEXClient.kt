package com.fermt.fintech.screener.input.client

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
        return 1.0
    }
}