package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import java.io.File

val blockedEndpoints = mutableSetOf<String>()
const val BLOCKED_PATHS_FILE = "blocked_endpoints.txt"

fun Application.configureTrafficControl() {
    loadBlockedEndpoints()

    routing {
        post("/proxy/block") {
            val endpoint = call.receive<String>()
            blockedEndpoints.add(endpoint)
            saveBlockedEndpoints()
            call.respond(HttpStatusCode.OK, "Blocked $endpoint")
        }
        post("/proxy/unblock") {
            val endpoint = call.receive<String>()
            blockedEndpoints.remove(endpoint)
            saveBlockedEndpoints()
            call.respond(HttpStatusCode.OK, "Unblocked $endpoint")
        }
        get("/proxy/blocked") {
            call.respond(HttpStatusCode.OK, blockedEndpoints.joinToString(", "))
        }
    }
}

fun loadBlockedEndpoints() {
    val file = File(BLOCKED_PATHS_FILE)
    if (file.exists()) {
        blockedEndpoints.clear()
        blockedEndpoints.addAll(file.readLines().filter { it.isNotBlank() })
    }
}

fun saveBlockedEndpoints() {
    File(BLOCKED_PATHS_FILE).writeText(blockedEndpoints.joinToString("\n"))
}