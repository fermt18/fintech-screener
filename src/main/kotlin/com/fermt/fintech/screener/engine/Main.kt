package com.fermt.fintech.screener.engine

import com.fermt.fintech.screener.input.DataFeed
import com.fermt.fintech.screener.output.CSVFile
import java.io.File
import java.nio.file.Paths

fun main() {
    val uriPath = Paths.get("${System.getProperty("user.dir")}" +
            "${File.separator}src" +
            "${File.separator}main" +
            "${File.separator}resources" +
            "${File.separator}tickers" +
            "${File.separator}tickerList").toUri()
    val df = DataFeed(uriPath)
    val tickerList = df.getTickerList()
    val stockList = df.getStockList(tickerList)
    val f = CSVFile(stockList).create()
    //Console(f).print()
}