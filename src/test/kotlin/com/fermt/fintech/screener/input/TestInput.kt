package com.fermt.fintech.screener.input

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestInput {

    private val tl = TickerList(this::class.java.classLoader.getResource("tickers/tickerList").toURI())

    @Test
    fun testInputTickerList(){
        assertEquals(2, tl.read().size)
    }

    @Test
    fun testContentTickerList(){
        assertEquals("MSFT", tl.read()[0])
        assertEquals("GOOG", tl.read()[1])
    }
}