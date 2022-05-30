package com.fermt.fintech.screener.input

import com.fermt.fintech.screener.engine.Operations
import com.fermt.fintech.screener.input.client.IEXClient
import com.fermt.fintech.screener.model.Stock
import java.io.File
import java.net.URI
import kotlin.math.pow

class DataFeed(val path: URI) {

    val client = IEXClient()


    fun getTickerList(): List<String> {
        return File(path).readLines(Charsets.UTF_8)
    }

    fun getStockList(tl: List<String>): List<Stock> {
        val stockList = ArrayList<Stock>()
        for (t in tl) {
            stockList.add(fetchStock(t))
        }
        return stockList
    }

    private fun fetchStock(t: String): Stock {
        client.update(t)
        val sector = client.getSector()
        val ebit = client.getEBIT()
        val netIncome = client.getNetIncome()
        val incomeTax = client.getIncomeTax()
        val sharesOutstanding = client.getSharesOutstanding()
        val price = client.getPrice()
        val totalDebt = client.getTotalDebt()
        val cashEq = client.getCashAndCashEq()
        val currentAssets = client.getCurrentAssets()
        val totalAssets = client.getTotalAssets()
        val currentLiabilities = client.getCurrentLiabilities()
        val totalEquity = client.getTotalEquity()
        val opCashFlow = client.getOpCashFlow()
        val capex = client.getCapEx()

        val marketCap = Operations.calcMarketCap(price, sharesOutstanding)
        val fcff = Operations.calcFCF(opCashFlow, capex)
        val ev = Operations.calcEV(marketCap, totalDebt, cashEq)
        val currentRatio = Operations.calcCurrentRatio(currentAssets, currentLiabilities)
        val debtToEquity = Operations.calcDebtToEquity(totalDebt, totalEquity)
        val roa = Operations.calcRoA(netIncome, totalAssets)
        val roe = Operations.calcRoE(netIncome, totalEquity)
        val roic = Operations.calcRoIC(ebit, totalAssets, currentLiabilities)
        val pe = Operations.calcPE(price, netIncome)
        val evebit = Operations.calcEVEBIT(ev, ebit)
        val evfcff = Operations.calcEVFCFF(ev, fcff)
        val taxRate = Operations.calcTaxRate(netIncome, incomeTax)
        val nopat = Operations.calcNOPAT(ebit, taxRate)
        val growthRate = 0.10
        val reinvestmentRate = 0.8
        val discountRate = 0.09
        val years = 10
        val growthRateTerminal = 0.03
        val reinvestmentRateTerminal = 0.2
        val discountRateTerminal = 0.04

        val iv = Operations.calcDFCFF(
            nopat,
            growthRate,
            reinvestmentRate,
            discountRate,
            years,
            nopat * (1 + growthRate).pow(years),
            growthRateTerminal,
            reinvestmentRateTerminal,
            discountRateTerminal)

        return Stock(
            ticker = t,
            sector = sector,
            marketCap = marketCap,
            ev = ev,
            currentRatio = currentRatio,
            debtToEquity = debtToEquity,
            roa = roa,
            roe = roe,
            roic = roic,
            pe = pe,
            evebit = evebit,
            evfcff = evfcff,
            iv = iv
        )
    }
}