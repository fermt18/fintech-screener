package com.fermt.fintech.screener.output

import com.fermt.fintech.screener.input.DataFeed
import com.fermt.fintech.screener.model.Stock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestCSVFile {

    private lateinit var stockList: List<Stock>

    @BeforeEach
    fun init(){
        val df = DataFeed(this::class.java.classLoader.getResource("tickers/tickerList").toURI())
        val tl = df.getTickerList()
        stockList = df.getStockList(tl)
    }

    @Test
    fun testCSVFile(){
        val f = CSVFile(stockList).create()
        assertTrue(f.exists())
        assertEquals(3, f.readLines(Charsets.UTF_8).size)
    }
}