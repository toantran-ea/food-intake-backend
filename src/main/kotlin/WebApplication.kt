package com.example.demo

import com.example.demo.data.DataRepository
import com.example.demo.data.fromJsonStringToList
import com.example.demo.data.toJsonString
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer

// Entry Point of the application as defined in resources/application.conf.
// @see https://ktor.io/servers/configuration.html#hocon-file

val dataRepo = DataRepository()

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        get("/") {
            call.respond("It works!")
        }

        get("entries") {
            call.respondText(contentType = ContentType.Application.Json) {
                dataRepo.listAll().toJsonString()
            }
        }

        post("entries") {
            val entries = fromJsonStringToList(call.receiveTextWithCorrectEncoding())
            try {
                val savedKeys = dataRepo.save(entries)
                call.respondText(contentType = ContentType.Application.Json) {
                    Json.stringify(String.serializer().list, savedKeys.map { it.name })
                }
            } catch (ex: Exception) {
                call.respond(status = HttpStatusCode.InternalServerError, message = ex.message
                        ?: "it went wrong!")
            }
        }
    }
}
