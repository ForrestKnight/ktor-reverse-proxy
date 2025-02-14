package com.example

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*
import kotlinx.serialization.json.*

val client = HttpClient()

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Reverse Proxy is Running!")
        }

        get("/proxy/{service}/{path...}") {
            val service = call.parameters["service"]
            val path = call.parameters.getAll("path")?.joinToString("/") ?: ""

            if (blockedEndpoints.contains("/$path")) {
                call.respond(HttpStatusCode.Forbidden, "Access to /$path is blocked.")
                return@get
            }

            if (service == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing service parameter")
                return@get
            }
            val services = getDockerServices()
            val backendUrl = services[service]?.plus(path) ?: "http://localhost:8000/$path"
            println("Proxying request to $backendUrl")

            try {
                val response = client.get(backendUrl)
                var responseBody = response.bodyAsText()

                responseBody = responseBody.replace("Hello", "Hi")

                call.respond(response.status, responseBody)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "Backend service unavailable: ${e.message}")
            }
        }
    }
}
