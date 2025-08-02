package io.github.catperson.projectceres.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener

object WelcomeScreen : Screen("welcome") {
    @Composable
    override fun CreateView(navController: NavController) {
        Scaffold(
            content = { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    val engine = rememberEngine()
                    val modelLoader = rememberModelLoader(engine)
                    val centerNode = rememberNode(engine)

                    val cameraNode = rememberCameraNode(engine) {
//                        position = Position(y = -0.5f, z = 2.0f)
                        lookAt(centerNode)
                        centerNode.addChildNode(this)
                    }
                    Scene(
                        modifier = Modifier.fillMaxWidth().height(400.dp),
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
                                    scaleToUnits = 1f
                                )
                            }),
//                        environment = environmentLoader.createHDREnvironment(
//                            assetFileLocation = "environments/sky_2k.hdr"
//                        )!!,
//                        onFrame = {
//                            centerNode.rotation = cameraRotation
//                            cameraNode.lookAt(centerNode)
//                        },
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
                        Text("Go to Details")
                    }
                }
            }
        )
    }
}