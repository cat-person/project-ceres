package io.github.catperson.projectceres.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.romainguy.kotlin.math.Float3
import io.github.catperson.projectceres.local.Cache
import io.github.catperson.projectceres.remote.NasaRemote
import io.github.catperson.projectceres.remote.StellarBody
import io.github.sceneview.Scene
import io.github.sceneview.collision.Vector3
import io.github.sceneview.model.Model
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.PlaneNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeRepo(private val ceresRemote: NasaRemote, private val cache: Cache) {
    suspend fun getCeresDistance(): StellarBody {
        return ceresRemote.getCeresDistance();
    }
}

class WelcomeVM(private val repo: WelcomeRepo) : ViewModel() {
    private val _stateFlow = MutableStateFlow(State(StellarBody.None))
    val state = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val stellarBody = repo.getCeresDistance()
            _stateFlow.value = State(stellarBody)
        }
    }

    data class State(val stellarBody: StellarBody)
    // Define ViewModel factory in a companion object
}

//val start = Position(0f, 0f, -2f) // Start point
//val end = Position(1f, 1f, -2f)   // End point
//
//// Calculate direction and length
//val direction = end - start
//val length = direction.length()
//
//// Calculate midpoint
//val midpoint = (start + end) * 0.5f
//
//// Create rotation from default "up" (Y-axis) to the line's direction
//val rotation = Quaternion.fromToRotation(
//    Vector3.up(),
//    direction.normalized()
//)
//
//// Create a thin cylinder (line)
//val lineNode = ModelNode().apply {
//    position = midpoint
//    this.rotation = rotation
//    model = Model(
//        shape = ShapeCylinder(
//            center = Vector3.zero(),
//            height = length,
//            radius = 0.005f // Adjust thickness here
//        ),
//        material = ColorMaterial().apply {
//            baseColor = Color(android.graphics.Color.BLUE)
//        }
//    )
//}

object WelcomeScreen : Screen<WelcomeVM>("welcome") {
    @Composable
    override fun CreateView(navController: NavController, vm: WelcomeVM) {

        val state = vm.state.collectAsState()
        val stellarBody = state.value.stellarBody
        Scaffold(
            content = { innerPadding ->
                Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    val engine = rememberEngine()
                    val modelLoader = rememberModelLoader(engine)

                    val coordNode = PlaneNode(engine)
                    val modelNode = ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "Ceres.glb"
                        ),
                        scaleToUnits = 0.6f
                    )

                    Scene(
                        modifier = Modifier.fillMaxWidth().height(480.dp),
                        engine = engine,
                        childNodes = listOf(coordNode, modelNode),
                    )

                    Button(
                        onClick = {
                            navController.navigate(DetailsScreen.route)
                        }
                    ) {
                        Text("Details")
                    }

                    Row() {
                        Box(modifier = Modifier.weight(1f)) {
                            Text("Radius")
                        }
                        Text("${stellarBody.perihelion}")
                    }
                    Row() {
                        Box(modifier = Modifier.weight(1f)) {
                            Text("Mass")
                        }
                        Text("${stellarBody.mass.massValue}*10^${stellarBody.mass.massExponent}")
                    }

                    Row() {
                        Box(modifier = Modifier.weight(1f)) {
                            Text("Distance")
                        }
                        Text("${stellarBody.aphelion}")
                    }

                }
            }
        )
    }
}