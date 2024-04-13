package com.ammar.quickswitchmanager.ui.screens.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.quickswitchmanager.common.PackageDetails
import com.ammar.quickswitchmanager.extensions.TAG
import com.ammar.quickswitchmanager.extensions.getAllRecentsProviders
import com.ammar.quickswitchmanager.extensions.getCurrentLauncher
import com.ammar.quickswitchmanager.utils.NoRootException
import com.ammar.quickswitchmanager.utils.QS_OVERLAY_PACKAGE_NAME
import com.ammar.quickswitchmanager.utils.filterRecentCompIDMaps
import com.ammar.quickswitchmanager.utils.getCurrentRecentsProviderPackageName
import com.ammar.quickswitchmanager.utils.getOverlayIDMaps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val application: Application,
) : AndroidViewModel(
    application = application,
) {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun init() = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        val recentCompIDMaps = try {
            filterRecentCompIDMaps(getOverlayIDMaps())
        } catch (e: NoRootException) {
            Log.e(TAG, "init: ", e)
            _uiState.update {
                it.copy(
                    loading = false,
                    hasRoot = false,
                )
            }
            return@launch
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching overlay IDMaps", e)
            // check root if cmd fails
            emptyList()
        }
        val idMapToLookup = recentCompIDMaps
            .find { it.packageName == QS_OVERLAY_PACKAGE_NAME }
            ?: recentCompIDMaps.firstOrNull()
        val currentRecentsProviderPackageName = idMapToLookup?.let {
            try {
                getCurrentRecentsProviderPackageName(it)
            } catch (e: Exception) {
                Log.e(TAG, "Error in overlay lookup: ", e)
                null
            }
        }
        val currentHome = application.getCurrentLauncher()
        val recentsProviders = application.getAllRecentsProviders()
            .map {
                val packageName = it.packageName
                PackageDetails(
                    packageName = packageName,
                    icon = it.applicationInfo.loadIcon(application.packageManager),
                    label = it.applicationInfo.loadLabel(application.packageManager).toString(),
                    isCurrentRecentsProvider = packageName == currentRecentsProviderPackageName,
                    isCurrentHome = packageName == currentHome?.activityInfo?.packageName,
                )
            }
            .sortedBy { it.label }
            .groupBy {
                if (it.isCurrentRecentsProvider) {
                    "current"
                } else {
                    "others"
                }
            }
            .filter { it.value.isNotEmpty() }
            .mapValues { it.value.toImmutableList() }
        _uiState.update {
            it.copy(
                loading = false,
                initDone = true,
                recentsProviders = recentsProviders.toImmutableMap(),
            )
        }
    }

    fun changeProvider(packageDetails: PackageDetails) {
        Log.d(TAG, "changeProvider: $packageDetails")
    }
}

@Stable
data class HomeScreenUiState(
    val hasRoot: Boolean = true,
    val loading: Boolean = true,
    val initDone: Boolean = false,
    val recentsProviders: ImmutableMap<String, ImmutableList<PackageDetails>> = persistentMapOf(),
)
