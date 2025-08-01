//package io.github.catperson.projectceres.ui
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavController
//
//object WelcomeScreen : Screen("welcome") {
//    @Composable
//    override fun CreateView(navController: NavController) {
//        Scaffold(
//            content = { innerPadding ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(innerPadding),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Button(
//                        onClick = {
//                            navController.navigate(DetailsScreen.route)
//                        }
//                    ) {
//                        Text("Go to Screen B")
//                    }
//                }
//            }
//        )
//    }
//}