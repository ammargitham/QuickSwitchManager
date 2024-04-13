package com.ammar.quickswitchmanager.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ammar.quickswitchmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavHostController,
    onHome: Boolean = false,
    isCmdRunning: Boolean = false,
    showSaveLogsAction: Boolean = false,
    onChangeDefaultHomeClick: () -> Unit = {},
    onSaveLogsClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            if (!onHome) {
                IconButton(
                    enabled = !isCmdRunning,
                    onClick = { navController.popBackStack() },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            TopAppBarActions(
                showMoreOptions = onHome,
                showSaveLogsAction = !onHome && showSaveLogsAction,
                onChangeDefaultHomeClick = onChangeDefaultHomeClick,
                onSaveLogsClick = onSaveLogsClick,
            )
        },
    )
}
