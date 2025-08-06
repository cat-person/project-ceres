package io.github.catperson.projectceres.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

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
//    val name: String,
    val englishName: String,
//    val isPlanet: Boolean,
//    val semimajorAxis: Double,
    val perihelion: Double,
    val aphelion: Double,
//    val equaRadius: Double,
//    val eccentricity: Double,
//    val inclination: Double,
//    val bodyType: String,
    val mass: Mass,
) {
    companion object {
        val None = StellarBody("none", "none", 0.0, 0.0, Mass.Unknown)
    }
}

@Serializable
data class Mass(val massValue: Float, val massExponent: Int) {
    companion object {
        val Unknown = Mass(-1f, 0)
    }
}

class NasaRemote(private val client: HttpClient) {
    suspend fun getCeresDistance(): StellarBody {
        client.use { client ->
            val response = client.get(HORIZONS_API)
            Log.e("AAAAA", "${response.body<JsonObject>()}")
            return response.body<StellarBody>()
        }
    }
}

//{
//    "id":"ceres",
//    "name":"(1) Cérès",
//    "englishName":"1 Ceres",
//    "isPlanet":false,
//    "moons":null,
//    "semimajorAxis":414010000,
//    "perihelion":382620000,
//    "aphelion":445410000,
//    "eccentricity":0.07582,
//    "inclination":10.59300,
//    "mass":{"massValue":9.39300,"massExponent":20},
//    "vol":{"volValue":4.21000,"volExponent":9},
//    "density":2.16100,
//    "gravity":0.28000,
//    "escape":510.00000,
//    "meanRadius":476.20000,
//    "equaRadius":487.00000,
//    "polarRadius":455.00000,
//    "flattening":0.96190,
//    "dimension":"",
//    "sideralOrbit":1681.63000,
//    "sideralRotation":9.07000,
//    "aroundPlanet":null,
//    "discoveredBy":"Giuseppe Piazzi",
//    "discoveryDate":"01/01/1801",
//    "alternativeName":"A899 OF - 1943 XB",
//    "axialTilt":3,
//    "avgTemp":168,
//    "mainAnomaly":77.37210,
//    "argPeriapsis":73.59769,
//    "longAscNode":80.30553,
//    "bodyType":"Asteroid"
//}
