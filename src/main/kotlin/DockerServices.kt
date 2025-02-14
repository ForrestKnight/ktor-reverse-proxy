package com.example

import java.io.BufferedReader
import java.io.InputStreamReader

fun getDockerServices(): Map<String, String> {
    val serviceMap = mutableMapOf<String, String>()
    try {
        val process = ProcessBuilder("docker", "ps", "--format", "{{.Names}} {{.Ports}}").start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(" ")
                if (parts.size >= 2) {
                    val serviceName = parts[0]
                    val portsInfo = parts.drop(1).joinToString(" ")
                    val hostPortRegex = Regex("(\\d+)->\\d+/tcp")

                    val hostPort = hostPortRegex.find(portsInfo)?.groups?.get(1)?.value
                    if (hostPort != null) {
                        serviceMap[serviceName] = "http://localhost:$hostPort/"
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("Error occurred while fetching Docker services: ${e.message}")
    }
    println("Service Map: $serviceMap")
    return serviceMap
}
