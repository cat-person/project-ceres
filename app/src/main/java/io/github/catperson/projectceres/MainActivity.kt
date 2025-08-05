package io.github.catperson.projectceres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.catperson.projectceres.local.Cache
import io.github.catperson.projectceres.remote.NasaRemote
import io.github.catperson.projectceres.ui.DetailsScreen
import io.github.catperson.projectceres.ui.DetailsVM
import io.github.catperson.projectceres.ui.WelcomeRepo
import io.github.catperson.projectceres.ui.WelcomeScreen
import io.github.catperson.projectceres.ui.WelcomeVM
import io.github.catperson.projectceres.ui.theme.ProjectCeresTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cache = Cache()
        val nasaRemote =  NasaRemote(HttpClient(CIO){
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }})

        val welcomeVM = WelcomeVM(repo = WelcomeRepo(nasaRemote, cache))
        val detailsVM = DetailsVM()

        setContent {
            ProjectCeresTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = WelcomeScreen.route
                ) {
                    composable(WelcomeScreen.route) { WelcomeScreen.CreateView(navController, welcomeVM) }
                    composable(DetailsScreen.route) { DetailsScreen.CreateView(navController, detailsVM) }
                }
            }
        }
    }
}