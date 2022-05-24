package com.fermt.fintech.screener.model

data class Stock(
    val ticker: String,
    val sector: String,
    val marketCap: Long,
    val ev: Long,
    // Debt
    val currentRatio: Double,
    val debtToEquity: Double,
    // Performance
    val roa: Double,
    val roe: Double,
    val roic: Double,
    // Valuation
    val pe: Double,
    val evebit: Double,
    val evfcff: Double,
    val iv: Double
)
