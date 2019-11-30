package com.example.demo

/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/* ktlint-disable no-wildcard-imports */
import com.example.demo.data.DataRepository
import com.example.demo.data.fromJsonStringToList
import com.example.demo.data.toJsonString
import com.google.appengine.api.datastore.Key
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.request.contentType
import io.ktor.request.receiveStream
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.utils.io.charsets.Charset
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.stringify

// Entry Point of the application as defined in resources/application.conf.
// @see https://ktor.io/servers/configuration.html#hocon-file


val dataRepo = DataRepository()

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        get("/") {
            call.respond("Hello World 111 !!")
        }

        get("entries") {
            call.respondText(contentType = ContentType.Application.Json) {
                dataRepo.listAll().toJsonString()
            }
        }

        post("entries") {
            val entries = fromJsonStringToList(call.receiveTextWithCorrectEncoding())
            val savedKeys = dataRepo.save(entries)
            call.respondText(contentType = ContentType.Application.Json) {
                Json.stringify(String.serializer().list, savedKeys.map { it.name })
            }
        }
    }
}

private suspend fun ApplicationCall.receiveTextWithCorrectEncoding(): String {
    fun ContentType.defaultCharset(): Charset = when (this) {
        ContentType.Application.Json -> Charsets.UTF_8
        else -> Charsets.ISO_8859_1
    }

    val contentType = request.contentType()
    val suitableCharset = contentType.charset() ?: contentType.defaultCharset()
    return receiveStream().bufferedReader(charset = suitableCharset).readText()
}

