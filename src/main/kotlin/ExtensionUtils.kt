package com.example.demo

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.request.contentType
import io.ktor.request.receiveStream
import io.ktor.utils.io.charsets.Charset

suspend fun ApplicationCall.receiveTextWithCorrectEncoding(): String {
    fun ContentType.defaultCharset(): Charset = when (this) {
        ContentType.Application.Json -> Charsets.UTF_8
        else -> Charsets.ISO_8859_1
    }

    val contentType = request.contentType()
    val suitableCharset = contentType.charset() ?: contentType.defaultCharset()
    return receiveStream().bufferedReader(charset = suitableCharset).readText()
}

