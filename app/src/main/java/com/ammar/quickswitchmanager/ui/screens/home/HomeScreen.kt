package com.ammar.quickswitchmanager.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ammar.quickswitchmanager.utils.QSCommands

@Composable
fun HomeScreen(
    navigationToShell: (String) -> Unit,
) {
    val viewModel: HomeScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    HomeScreenContent(
        loading = uiState.loading,
        hasRoot = uiState.hasRoot,
        current = uiState.recentsProviders["current"],
        others = uiState.recentsProviders["others"],
        onProviderClick = {
            val cmd = QSCommands.ch(it.packageName)
            navigationToShell(cmd)
        },
    )
}
