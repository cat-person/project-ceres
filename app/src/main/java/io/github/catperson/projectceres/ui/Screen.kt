package io.github.catperson.projectceres.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

abstract class Screen<T : ViewModel>(val route: String) {
    @Composable
    abstract fun CreateView (navController: NavController, vm: T)
}