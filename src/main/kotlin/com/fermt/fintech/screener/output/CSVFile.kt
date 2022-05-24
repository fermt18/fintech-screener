package com.fermt.fintech.screener.output

import com.fermt.fintech.screener.model.Headers
import com.fermt.fintech.screener.model.Stock
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths


class CSVFile(private val stockList: List<Stock>) {

    fun create(): File {
        val path = Paths.get(System.getProperty("user.dir"), "build", "output")
        Files.createDirectories(path)
        val file = File("$path${File.separator}screener.csv")
        val out = FileWriter(file)
        val b = CSVFormat.Builder.create()
        b.setHeader(Headers::class.java)
        val p = CSVPrinter(out, b.build())
        for(stock in stockList){
            p.printRecord(stock.ticker)
        }
        p.flush()
        p.close()
        return file
    }
}