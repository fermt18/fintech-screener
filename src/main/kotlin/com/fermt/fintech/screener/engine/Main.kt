package com.fermt.fintech.screener.engine

import com.fermt.fintech.screener.input.DataFeed
import com.fermt.fintech.screener.output.CSVFile
import java.nio.file.Paths

class Main {

    val df = DataFeed(Paths.get("").toUri())
    val tickerList = df.getTickerList()
    val stockList = df.getStockList(tickerList)
    val f = CSVFile(stockList).create()
    //Console(f).print()
}