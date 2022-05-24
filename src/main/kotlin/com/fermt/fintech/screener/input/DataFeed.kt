package com.fermt.fintech.screener.input

import com.fermt.fintech.screener.input.client.ClientInt
import com.fermt.fintech.screener.input.client.IEXClient
import com.fermt.fintech.screener.model.Stock

class DataFeed(val tl: List<String>) {

    val client = IEXClient()

    fun fetch(): List<Stock> {
        val stockList = ArrayList<Stock>()
        for (t in tl) {
            stockList.add(fetchStock(t))
        }
        return stockList
    }

    private fun fetchStock(t: String): Stock {
        return Stock(
            ticker = t,
            sector = client.getSector(t),
            marketCap = 1L,
            ev = 1L,
            currentRatio = 1.0,
            debtToEquity = 1.0,
            roa = 1.0,
            roe = 1.0,
            roic = 1.0,
            pe = 1.0,
            evebit = 1.0,
            evfcff = 1.0,
            iv = 1.0
        )
    }
}