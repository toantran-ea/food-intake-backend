package com.example.demo.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.parse
import java.util.*

@Serializable
data class FoodEntry(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val time: Long = System.currentTimeMillis(),
        val description: String,
        val energyCount: Long = 0,
        val updated: Long = System.currentTimeMillis()
)

fun FoodEntry.toJsonString(): String {
    return Json.stringify(FoodEntry.serializer(), this)
}

fun List<FoodEntry>.toJsonString(): String {
    return Json.stringify(FoodEntry.serializer().list, this)
}

fun fromJsonStringToObject(input: String): FoodEntry {
    return Json.parse(FoodEntry.serializer(), input)
}

fun fromJsonStringToList(input: String): List<FoodEntry> {
    return Json.parse(FoodEntry.serializer().list, input)
}


