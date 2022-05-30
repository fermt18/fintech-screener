package com.fermt.fintech.screener.input.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File


class IEXClient: ClientInt {

    private val client = HttpClient(CIO)
    private val token: String
    private lateinit var company: JSONObject
    private lateinit var incomeStatement: JSONObject


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

    fun update(ticker: String){
        runBlocking {
            company = sendRequest("/stock/$ticker/company")
            incomeStatement = sendRequest("/stock/$ticker/income").getJSONArray("income").get(0) as JSONObject
        }
    }

    override fun getSector(ticker: String): String {
        return company.get("sector").toString()
    }

    override fun getEBIT(ticker: String): Long {
        return incomeStatement.getLong("ebit")
    }

    override fun getNetIncome(ticker: String): Long {
        return incomeStatement.getLong("netIncome")
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

    private suspend fun sendRequest(endpoint: String): JSONObject{
        val baseUrl = "https://sandbox.iexapis.com/stable"
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return JSONObject(resp.body() as String)
    }
}