package com.fermt.fintech.screener.input

import java.io.File
import java.net.URI

class TickerList(val path: URI) {

    fun read(): List<String> {
        return File(path).readLines(Charsets.UTF_8)
    }
}