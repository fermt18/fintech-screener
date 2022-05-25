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
            marketCap = client.getMarketCap(t),
            ev = client.getEV(t),
            currentRatio = client.getCurrentRatio(t),
            debtToEquity = client.getDebtToEquity(t),
            roa = client.getRoA(t),
            roe = client.getRoE(t),
            roic = client.getRoIC(t),
            pe = client.getPE(t),
            evebit = client.getEVEBIT(t),
            evfcff = client.getEVFCFF(t),
            iv = client.getIV(t)
        )
    }
}