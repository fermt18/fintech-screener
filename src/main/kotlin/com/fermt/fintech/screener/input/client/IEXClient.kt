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
    private val baseUrl: String
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
            baseUrl = "https://sandbox.iexapis.com/stable"
        }
        else{
            token = configPars.filter { s -> s.startsWith("pro_token") }.first().split("=")[1]
            baseUrl = "https://cloud.iexapis.com/stable"
        }
    }

    override fun update(ticker: String): Boolean {
        var r = true
        runBlocking {
            priceOnly = getPrice(ticker).toDouble()                                  //    1/call
            company = sendRequest("/stock/$ticker/company")                  //   10/symbol
            stats = sendRequest("/stock/$ticker/stats")                      //    5/symbol, full stats; 1/symbol, single stat
            val iSt = sendRequestWithPeriod("/stock/$ticker/income", "annual", "1")
            if(iSt.isEmpty){
                incomeStatement = JSONObject()
                balanceSheet = JSONObject()
                cashFlow = JSONObject()
                r = false
                return@runBlocking
            }
            incomeStatement = iSt.getJSONArray("income").get(0) as JSONObject            // 1000/symbol/period
            balanceSheet = sendRequestWithPeriod("/stock/$ticker/balance-sheet", "annual", "1").getJSONArray("balancesheet").get(0) as JSONObject  // 3000/symbol/period
            cashFlow = sendRequestWithPeriod("/stock/$ticker/cash-flow", "annual", "1").getJSONArray("cashflow").get(0) as JSONObject              // 1000/symbol/period
        }
        return r
        // 5016/symbol/period
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
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return JSONObject(resp.body() as String)
    }


    private suspend fun sendRequestWithPeriod(endpoint: String, period: String, last: String): JSONObject {
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
            parameter("period", period)
            parameter("last", last)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return JSONObject(resp.body() as String)
    }

    private suspend fun getPrice(ticker: String): String {
        val endpoint = "/stock/$ticker/price"
        val resp: HttpResponse = client.get("$baseUrl$endpoint") {
            parameter("token", token)
        }
        if(resp.status != HttpStatusCode.OK)
            println("HTTP with unexpected response: ${resp.status}")
        return resp.body()
    }
}