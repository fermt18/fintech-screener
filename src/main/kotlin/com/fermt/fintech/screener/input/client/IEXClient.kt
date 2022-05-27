package com.fermt.fintech.screener.input.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.File


class IEXClient: ClientInt {

    val client = HttpClient(CIO)
    lateinit var token: String

    init {
        val configPars = File("${System.getProperty("user.dir")}${File.separator}config.cfg").readLines()
            .map { s -> s.replace(" ", "") }
        if(configPars.filter { s -> s.startsWith("environment") }.first().split("=")[1].equals("dev")){
            token = configPars.filter { s -> s.startsWith("dev_token") }.first().split("=")[1]
        }
        else{
            token = configPars.filter { s -> s.startsWith("pro_token") }.first().split("=")[1]
        }
    }

    override fun getSector(ticker: String): String {
        val baseUrl = "https://sandbox.iexapis.com/stable"
        val url = "$baseUrl/stock/$ticker/company"
        val resp: String
        runBlocking {
            resp = sendRequest(url)
        }
        val jo = JSONObject(resp)
        return jo.get("sector").toString()
    }

    private suspend fun sendRequest(url: String): String{
        val resp: HttpResponse = client.get(url) {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return resp.body()
    }

    override fun getEBIT(ticker: String): Long {
        return 100L
    }

    override fun getNetIncome(ticker: String): Long {
        return 10L
    }

    override fun getIncomeTax(ticker: String): Long {
        return 35L
    }

    override fun getSharesOutstanding(ticker: String): Long {
        return 3500000L
    }

    override fun getPrice(ticker: String): Double {
        return 5.27
    }

    override fun getTotalDebt(ticker: String): Long {
        return 27L
    }

    override fun getCashAndCashEq(ticker: String): Long {
        return 3L
    }

    override fun getCurrentAssets(ticker: String): Long {
        return 185L
    }

    override fun getTotalAssets(ticker: String): Long {
        return 1850L
    }

    override fun getCurrentLiabilities(ticker: String): Long {
        return 15L
    }

    override fun getTotalEquity(ticker: String): Long {
        return 5L
    }

    override fun getOpCashFlow(ticker: String): Long {
        return 8L
    }

    override fun getCapEx(ticker: String): Long {
        return 2L
    }

}