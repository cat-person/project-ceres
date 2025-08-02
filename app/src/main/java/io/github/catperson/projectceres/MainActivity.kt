package io.github.catperson.projectceres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.catperson.projectceres.ui.DetailsScreen
import io.github.catperson.projectceres.ui.WelcomeScreen
import io.github.catperson.projectceres.ui.theme.ProjectCeresTheme
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjectCeresTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = WelcomeScreen.route
                ) {
                    composable(WelcomeScreen.route) { WelcomeScreen.CreateView(navController) }
                    composable(DetailsScreen.route) { DetailsScreen.CreateView(navController) }
                }
            }
        }
    }
}