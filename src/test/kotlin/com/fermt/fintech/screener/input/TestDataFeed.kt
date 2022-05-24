package com.fermt.fintech.screener.input

import com.fermt.fintech.screener.model.Stock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestDataFeed {

    private lateinit var stockList: List<Stock>

    @BeforeEach
    fun init(){
        val tl = TickerList(this::class.java.classLoader.getResource("tickers/tickerList").toURI())
        stockList = DataFeed(tl.read()).fetch()
    }

    @Test
    fun testDataFeed(){
        assertEquals(2, stockList.size)
    }

    @Test
    fun testDataFeed_Ticker(){
        assertEquals("MSFT", stockList[0].ticker)
    }

    @Test
    fun testDataFeed_Sector(){
        assertEquals("Digital Ads", stockList[0].sector)
    }

    @Test
    fun testDataFeed_MarketCap(){
        assertEquals(1L, stockList[0].marketCap)
    }

    @Test
    fun testDataFeed_EV(){
        assertEquals(1L, stockList[0].ev)
    }
}