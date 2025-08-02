package io.github.catperson.projectceres.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// NASA JPL Horizons API endpoint
const val HORIZONS_API = "https://ssd-api.jpl.nasa.gov/horizons.api"

@Serializable
data class HorizonsResponse(
    val signature: Signature,
    val data: Data
)

@Serializable
data class Signature(
    val source: String,
    val version: String
)

@Serializable
data class Data(
    val vectors: List<Vector>
)

@Serializable
data class Vector(
    val delta: String,  // Distance in AU
    val delta_unit: String
)

suspend fun getCeresDistance(): Double {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Calculate dates for API request
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val dateFormat = DateTimeFormatter.ISO_DATE

    try {
        val response: HorizonsResponse = client.get(HORIZONS_API) {
            url {
                parameters.apply {
                    append("format", "json")
                    append("COMMAND", "'1'")       // Ceres JPL ID
                    append("OBJ_DATA", "'NO'")
                    append("MAKE_EPHEM", "'YES'")
                    append("EPHEM_TYPE", "'VECTORS'")
                    append("CENTER", "'500@399'")  // Earth observer
                    append("START_TIME", "'${today.format(dateFormat)}'")
                    append("STOP_TIME", "'${tomorrow.format(dateFormat)}'")
                    append("STEP_SIZE", "'1d'")
                    append("QUANTITIES", "'1'")    // Distance in AU
                }
            }
        }.body()

        // Extract distance from first vector data point
        val vectorData = response.data.vectors.first()
        val auDistance = vectorData.delta.toDouble()

        // Convert AU to kilometers (1 AU = 149,597,870.7 km)
        return auDistance * 149_597_870.7
    } finally {
        client.close()
    }
}

suspend fun getDistance() {
    try {
        val distance = getCeresDistance()
        println("Current distance from Earth to Ceres: ${"%.2f".format(distance)} km")
    } catch (e: Exception) {
        println("Error fetching data: ${e.message}")
    }
}