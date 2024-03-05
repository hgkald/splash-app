package no.uio.ifi.in2000.team22.badeapp.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson

class TestInternet {
    private val path = "weatherapi/metalerts/2.0/test.json"

    private val client =
        HttpClient {
            defaultRequest {
                url("https://gw-uio.intark.uh-it.no/in2000/")
                header("X-Gravitee-API-Key", "1257d958-d771-4767-abb0-9d78c0f45025")
            }

            install(ContentNegotiation) {
                gson()
            }
        }

    suspend fun hent(): Test{
        val response = client.get(path)
        val body = response.body<Test>()
        return body
    }
}

data class Test (
    val lang : String,
    val lastChange : String,
    val type : String,
)

suspend fun main(){
    val data = TestInternet()
    val verdi = data.hent()

    println(verdi)
}