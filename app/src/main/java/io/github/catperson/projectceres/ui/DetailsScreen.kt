package io.github.catperson.projectceres.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class DetailsRepo() {

}

class DetailsVM() : ViewModel() {
    // Define ViewModel factory in a companion object
}


object DetailsScreen : Screen<DetailsVM>("details") {
    @Composable
    override fun CreateView(navController: NavController, vm: DetailsVM) {
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
                            navController.popBackStack(DetailsScreen.route, true)
                        }
                    ) {
                        Text("Go to Welcome")
                    }
                }
            }
        )
    }
}