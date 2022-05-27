package com.fermt.fintech.screener.input

import com.fermt.fintech.screener.model.Stock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestDataFeed {

    private lateinit var tl: List<String>
    private lateinit var sl: List<Stock>

    @BeforeEach
    fun init(){
        val df = DataFeed(this::class.java.classLoader.getResource("tickers/tickerList").toURI())
        tl = df.getTickerList()
        sl = df.getStockList(tl)
    }

    @Test
    fun testInputTickerList(){
        assertEquals(2, tl.size)
    }

    @Test
    fun testContentTickerList(){
        assertEquals("MSFT", tl[0])
        assertEquals("GOOG", tl[1])
    }


    @Test
    fun testDataFeed(){
        assertEquals(2, sl.size)
    }

    @Test
    fun testDataFeed_Ticker(){
        assertEquals("MSFT", sl[0].ticker)
    }
}