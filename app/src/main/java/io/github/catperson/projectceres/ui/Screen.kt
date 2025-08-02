package io.github.catperson.projectceres.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

abstract class Screen(val route: String) {
    @Composable
    abstract fun CreateView (navController: NavController)
}