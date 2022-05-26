package com.fermt.fintech.screener.input.client

interface ClientInt {

    fun getSector(ticker: String): String
    fun getEBIT(ticker: String): Long
    fun getNetIncome(ticker: String): Long
    fun getIncomeTax(ticker: String): Long
    fun getSharesOutstanding(ticker: String): Long
    fun getPrice(ticker: String): Double
    fun getTotalDebt(ticker: String): Long
    fun getCashAndCashEq(ticker: String): Long
    fun getCurrentAssets(ticker: String): Long
    fun getTotalAssets(ticker: String): Long
    fun getCurrentLiabilities(ticker: String): Long
    fun getTotalEquity(ticker: String): Long
    fun getOpCashFlow(ticker: String): Long
    fun getCapEx(ticker: String): Long
}