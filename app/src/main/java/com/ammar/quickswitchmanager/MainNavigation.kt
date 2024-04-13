package com.ammar.quickswitchmanager

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ammar.quickswitchmanager.extensions.urlEncode
import com.ammar.quickswitchmanager.ui.screens.home.HomeScreen
import com.ammar.quickswitchmanager.ui.screens.shell.ShellScreen

const val HomeRoute = "home"
private const val commandArg = "command"

fun NavGraphBuilder.homeScreen(
    navigationToShell: (String) -> Unit,
) {
    composable(HomeRoute) {
        HomeScreen(
            navigationToShell = navigationToShell,
        )
    }
}

fun NavGraphBuilder.shellScreen(
    onCommandRunningChange: (Boolean) -> Unit,
    onShowSaveLogsActionChange: (Boolean, List<String>) -> Unit,
) {
    composable("shell/{$commandArg}") {
        ShellScreen(
            onCommandRunningChange = onCommandRunningChange,
            onShowSaveLogsActionChange = onShowSaveLogsActionChange,
        )
    }
}

fun NavController.navigateToShellScreen(command: String) {
    this.navigate("shell/${command.urlEncode()}")
}
