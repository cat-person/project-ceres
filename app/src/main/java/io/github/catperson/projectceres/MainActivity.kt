package io.github.catperson.projectceres

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.catperson.projectceres.ui.theme.ProjectCeresTheme

// Define sealed class for routes
sealed class Screen(val route: String) {
    object A : Screen("screen_a")
    object B : Screen("screen_b")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProjectCeresTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "screen_a"
                ) {
                    composable("screen_a") { ScreenA(navController) }
                    composable("screen_b") { ScreenB(navController) }
                }
            }
        }
    }
}

@Composable
fun ScreenA(navController: NavController) {
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Clear back stack to prevent accumulation
                        navController.navigate(Screen.B.route)
                    }
                ) {
                    Text("Go to Screen B")
                }
            }
        }
    )
}

// ScreenB.kt
@Composable
fun ScreenB(navController: NavController) {
    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Navigate back with back stack management
                        navController.navigate(Screen.A.route)
                    }
                ) {
                    Text("Go Back to Screen A")
                }
            }
        }
    )
}
