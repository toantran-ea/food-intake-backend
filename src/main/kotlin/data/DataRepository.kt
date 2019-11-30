package com.example.demo.data

import com.google.appengine.api.datastore.*

class DataRepository {
    private val dataStore = DatastoreServiceFactory.getDatastoreService()
    private val foodIntakeEntry = dataStore.prepare(Query(FOOD_INTAKE_ENTRY))

    // READ
    fun listAll(): List<FoodEntry> =
            foodIntakeEntry.asList(FetchOptions.Builder.withDefaults()).map { it.toEntry() }

    // CREATE/UPDATE
    fun save(entries: List<FoodEntry>): List<Key> {
        entries.map { it.toEntity() }.also { return dataStore.put(it) }
    }

    private fun FoodEntry.toEntity() = Entity(FOOD_INTAKE_ENTRY, id).apply {
        setProperty("id", id)
        setProperty("name", name)
        setProperty("time", time)
        setProperty("description", description)
        setProperty("energyCount", energyCount)
        setProperty("updated", updated)
    }

    private fun Entity.toEntry() = FoodEntry(
            id = getProperty("id") as String,
            name = getProperty("name") as String,
            description = getProperty("description") as String,
            time = getProperty("time") as Long,
            energyCount = getProperty("energyCount") as Long,
            updated = getProperty("updated") as Long
    )

    companion object {
        const val FOOD_INTAKE_ENTRY = "FoodIntakeEntry"
    }
}