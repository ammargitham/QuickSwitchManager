package com.ammar.quickswitchmanager

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ammar.quickswitchmanager.extensions.TAG
import com.ammar.quickswitchmanager.extensions.toast
import com.ammar.quickswitchmanager.ui.common.MainTopBar
import com.ammar.quickswitchmanager.ui.theme.QuickSwitchManagerRippleTheme
import com.ammar.quickswitchmanager.ui.theme.QuickSwitchManagerTheme
import com.ammar.quickswitchmanager.utils.LogUtils
import com.ammar.quickswitchmanager.utils.MutableListOfStringSaver

class MainActivity : ComponentActivity() {

    init {
        // Shell.enableVerboseLogging = BuildConfig.DEBUG
        if (BuildConfig.DEBUG) {
            try {
                Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", Boolean::class.javaPrimitiveType)
                    .invoke(null, true)
            } catch (e: Exception) {
                Log.e(TAG, "Error while enabling CloseGuard", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickSwitchManagerTheme {
                val navController = rememberNavController()
                var isCmdRunning by rememberSaveable { mutableStateOf(false) }
                var showSaveLogsAction by rememberSaveable { mutableStateOf(false) }
                var logs by rememberSaveable(
                    saver = MutableListOfStringSaver,
                ) { mutableStateOf(emptyList()) }
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val onHome by remember {
                    derivedStateOf {
                        val route = currentBackStackEntry?.destination?.route
                        route == null || route == HomeRoute
                    }
                }
                val createDocumentLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.CreateDocument("*/*"),
                ) {
                    if (it == null) {
                        return@rememberLauncherForActivityResult
                    }
                    LogUtils.writeToUri(this@MainActivity, it, logs)
                    toast("Logs saved!")
                }

                CompositionLocalProvider(
                    LocalRippleTheme provides QuickSwitchManagerRippleTheme,
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        Scaffold(
                            topBar = {
                                MainTopBar(
                                    onHome = onHome,
                                    isCmdRunning = isCmdRunning,
                                    navController = navController,
                                    showSaveLogsAction = showSaveLogsAction,
                                    onChangeDefaultHomeClick = {
                                        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                        startActivity(intent)
                                    },
                                    onSaveLogsClick = {
                                        createDocumentLauncher.launch(LogUtils.logFileName)
                                    },
                                )
                            },
                        ) {
                            NavHost(
                                modifier = Modifier.padding(it),
                                navController = navController,
                                startDestination = HomeRoute,
                            ) {
                                homeScreen(
                                    navigationToShell = { command ->
                                        navController.navigateToShellScreen(command)
                                    },
                                )
                                shellScreen(
                                    onCommandRunningChange = { running ->
                                        isCmdRunning = running
                                    },
                                    onShowSaveLogsActionChange = { show, l ->
                                        showSaveLogsAction = show
                                        logs = l
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
