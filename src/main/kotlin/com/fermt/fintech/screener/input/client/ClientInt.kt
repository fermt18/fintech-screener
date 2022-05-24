package com.fermt.fintech.screener.input.client

interface ClientInt {

    fun getSector(ticker: String): String
    fun getMarketCap(ticker: String): Long
    fun getEV(ticker: String): Long
    fun getCurrentRatio(ticker: String): Double
    fun getDebtToEquity(ticker: String): Double
    fun getRoA(ticker: String): Double
    fun getRoE(ticker: String): Double
    fun getRoIC(ticker: String): Double
    fun getPE(ticker: String): Double
    fun getEVEBIT(ticker: String): Double
    fun getEVFCFF(ticker: String): Double
    fun getIV(ticker: String): Double
}