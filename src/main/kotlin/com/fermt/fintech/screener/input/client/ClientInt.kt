package com.fermt.fintech.screener.input.client

interface ClientInt {

    fun update(ticker: String): Boolean
    fun getSector(): String
    fun getEBIT(): Long
    fun getNetIncome(): Long
    fun getIncomeTax(): Long
    fun getSharesOutstanding(): Long
    fun getPrice(): Double
    fun getTotalDebt(): Long
    fun getCashAndCashEq(): Long
    fun getCurrentAssets(): Long
    fun getTotalAssets(): Long
    fun getCurrentLiabilities(): Long
    fun getTotalEquity(): Long
    fun getOpCashFlow(): Long
    fun getCapEx(): Long
}