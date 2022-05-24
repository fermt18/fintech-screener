package com.fermt.fintech.screener.input

import com.fermt.fintech.screener.model.Stock

class DataFeed(val tl: List<String>) {

    fun fetch(): List<Stock> {
        val stockList = ArrayList<Stock>()
        for (t in tl) {
            stockList.add(fetchStock(t))
        }
        return stockList
    }

    private fun fetchStock(t: String): Stock {
        return Stock(
            ticker = t
        )
    }
}