package com.ammar.quickswitchmanager.ui.screens.shell

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShellScreen(
    onCommandRunningChange: (Boolean) -> Unit,
    onShowSaveLogsActionChange: (Boolean, List<String>) -> Unit,
) {
    val viewModel: ShellScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    LaunchedEffect(Unit) {
        if (uiState.running || uiState.completed) {
            return@LaunchedEffect
        }
        // runCmd only once
        viewModel.runCmd()
    }

    LaunchedEffect(uiState.running) {
        onCommandRunningChange(uiState.running)
        if (view.isInEditMode) {
            return@LaunchedEffect
        }
        val window = (view.context as Activity).window
        if (uiState.running) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    DisposableEffect(uiState.completed) {
        onShowSaveLogsActionChange(uiState.completed, uiState.output)
        onDispose {
            onShowSaveLogsActionChange(false, emptyList())
        }
    }

    BackHandler(
        enabled = uiState.running,
    ) {
        // ignore all back requests if command is running
        if (uiState.running) {
            return@BackHandler
        }
    }

    ShellScreenContent(
        lines = uiState.output,
        showRebootFAB = uiState.success,
        onRebootClick = viewModel::reboot,
    )
}
