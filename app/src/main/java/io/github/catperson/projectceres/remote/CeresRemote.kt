package io.github.catperson.projectceres.remote

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// NASA JPL Horizons API endpoint
const val HORIZONS_API = "https://api.le-systeme-solaire.net/rest/bodies/ceres"

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

@Serializable
data class StellarBody(
    val id: String,
    val name: String,
//    val englishName: String,
//    val isPlanet: Boolean,
//    val semimajorAxis: Double,
//    val perihelion: Double,
//    val aphelion: Double,
//    val equaRadius: Double,
//    val eccentricity: Double,
//    val inclination: Double,
//    val bodyType: String
)

//val client = HttpClient() {
//    install(ContentNegotiation) {
//        json(Json { ignoreUnknownKeys = true })
//    }
//}


//{"id":"ceres","name":"(1) Cérès","englishName":"1 Ceres","isPlanet":false,"moons":null,"semimajorAxis":414010000,"perihelion":382620000,"aphelion":445410000,"eccentricity":0.07582,"inclination":10.59300,"mass":{"massValue":9.39300,"massExponent":20},"vol":{"volValue":4.21000,"volExponent":9},"density":2.16100,"gravity":0.28000,"escape":510.00000,"meanRadius":476.20000,"equaRadius":487.00000,"polarRadius":455.00000,"flattening":0.96190,"dimension":"","sideralOrbit":1681.63000,"sideralRotation":9.07000,"aroundPlanet":null,"discoveredBy":"Giuseppe Piazzi","discoveryDate":"01/01/1801","alternativeName":"A899 OF - 1943 XB","axialTilt":3,"avgTemp":168,"mainAnomaly":77.37210,"argPeriapsis":73.59769,"longAscNode":80.30553,"bodyType":"Asteroid"}

class NasaRemote(private val client: HttpClient) {
    suspend fun getCeresDistance(): Double {
        // Calculate dates for API request
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val dateFormat = DateTimeFormatter.ISO_DATE

        client.use { client ->
            val response = client.get(HORIZONS_API)
//            {
//                url {
//                    parameters.apply {
//                        append("format", "json")
//                        append("COMMAND", "1")       // Ceres JPL ID
//                        append("OBJ_DATA", "NO")
//                        append("MAKE_EPHEM", "'YES'")
//                        append("EPHEM_TYPE", "VECTORS'")
//                        append("CENTER", "'500@399'")  // Earth observer
//                        append("START_TIME", "'${today.format(dateFormat)}'")
//                        append("STOP_TIME", "'${tomorrow.format(dateFormat)}'")
//                        append("STEP_SIZE", "'1d'")
//                        append("QUANTITIES", "'1'")    // Distance in AU
////                    }
//                }
//            }.body()

            Log.e("AAAA", "${response.body<StellarBody>()}")

            // Extract distance from first vector data point
//            val vectorData = response.data.vectors.first()
            val auDistance = 0.1// vectorData.delta.toDouble()

            // Convert AU to kilometers (1 AU = 149,597,870.7 km)
            return auDistance
        }
    }
}

