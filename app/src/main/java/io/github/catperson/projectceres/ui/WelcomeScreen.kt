package io.github.catperson.projectceres.ui

import android.os.Build
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import io.github.catperson.projectceres.local.Cache
import io.github.catperson.projectceres.remote.NasaRemote
import io.github.catperson.projectceres.remote.StellarBody
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
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
                    val centerNode = rememberNode(engine)

                    val cameraNode = rememberCameraNode(engine) {
                        lookAt(centerNode)
                        centerNode.addChildNode(this)
                    }
                    Scene(
                        modifier = Modifier.fillMaxWidth().height(480.dp),
                        engine = engine,
                        modelLoader = modelLoader,
                        cameraNode = cameraNode,
                        cameraManipulator = rememberCameraManipulator(
                            orbitHomePosition = cameraNode.worldPosition,
                            targetPosition = centerNode.worldPosition
                        ),
                        childNodes = listOf(centerNode,
                            rememberNode {
                                ModelNode(
                                    modelInstance = modelLoader.createModelInstance(
                                        assetFileLocation = "Ceres.glb"
                                    ),
                                    scaleToUnits = 0.6f
                                )
                            }),
                        onGestureListener = rememberOnGestureListener(
                            onDoubleTap = { _, node ->
                                node?.apply {
                                    scale *= 2.0f
                                }
                            }
                        )
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