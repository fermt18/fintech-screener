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
import kotlin.properties.Delegates


class IEXClient: ClientInt {

    private val client = HttpClient(CIO)
    private val token: String
    private var priceOnly by Delegates.notNull<Double>()
    private lateinit var company: JSONObject
    private lateinit var stats: JSONObject
    private lateinit var incomeStatement: JSONObject
    private lateinit var balanceSheet: JSONObject
    private lateinit var cashFlow: JSONObject


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

    override fun update(ticker: String){
        runBlocking {
            priceOnly = getPrice(ticker).toDouble()
            company = sendRequest("/stock/$ticker/company")
            stats = sendRequest("/stock/$ticker/stats")
            incomeStatement = sendRequest("/stock/$ticker/income").getJSONArray("income").get(0) as JSONObject
            balanceSheet = sendRequest("/stock/$ticker/balance-sheet").getJSONArray("balancesheet").get(0) as JSONObject
            cashFlow = sendRequest("/stock/$ticker/cash-flow").getJSONArray("cashflow").get(0) as JSONObject
        }
    }

    override fun getSector(): String {
        return company.get("sector").toString()
    }

    override fun getEBIT(): Long {
        return incomeStatement.getLong("ebit")
    }

    override fun getNetIncome(): Long {
        return incomeStatement.getLong("netIncome")
    }

    override fun getIncomeTax(): Long {
        return incomeStatement.getLong("incomeTax")
    }

    override fun getSharesOutstanding(): Long {
        return stats.getLong("sharesOutstanding")
    }

    override fun getPrice(): Double {
        return priceOnly
    }

    override fun getTotalDebt(): Long {
        return balanceSheet.getLong("currentLongTermDebt") +
                balanceSheet.getLong("longTermDebt")
    }

    override fun getCashAndCashEq(): Long {
        return balanceSheet.getLong("currentCash")
    }

    override fun getCurrentAssets(): Long {
        return balanceSheet.getLong("currentAssets")
    }

    override fun getTotalAssets(): Long {
        return balanceSheet.getLong("totalAssets")
    }

    override fun getCurrentLiabilities(): Long {
        return balanceSheet.getLong("totalCurrentLiabilities")
    }

    override fun getTotalEquity(): Long {
        return balanceSheet.getLong("shareholderEquity")
    }

    override fun getOpCashFlow(): Long {
        return cashFlow.getLong("cashFlow")
    }

    override fun getCapEx(): Long {
        return cashFlow.getLong("capitalExpenditures")
    }

    private suspend fun sendRequest(endpoint: String): JSONObject {
        val baseUrl = "https://sandbox.iexapis.com/stable"
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return JSONObject(resp.body() as String)
    }

    private suspend fun getPrice(ticker: String): String {
        val baseUrl = "https://sandbox.iexapis.com/stable"
        val endpoint = "/stock/$ticker/price"
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return resp.body()
    }
}