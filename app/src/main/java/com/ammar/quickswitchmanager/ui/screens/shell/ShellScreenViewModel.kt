package com.ammar.quickswitchmanager.ui.screens.shell

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.quickswitchmanager.extensions.TAG
import com.ammar.quickswitchmanager.extensions.urlDecode
import com.ammar.quickswitchmanager.utils.ShellUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShellScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val command get() = savedStateHandle.get<String>("command")?.urlDecode()
    private val _uiState = MutableStateFlow(ShellScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun runCmd() = viewModelScope.launch {
        val command = command ?: return@launch
        _uiState.update {
            it.copy(
                running = true,
                success = false,
                completed = false,
                output = persistentListOf(),
            )
        }
        withContext(Dispatchers.IO) {
            try {
                val result = ShellUtils.run(command) { out ->
                    _uiState.update {
                        it.copy(output = (it.output + out).toImmutableList())
                    }
                }
                _uiState.update {
                    it.copy(success = result.isSuccess)
                }
            } catch (e: Exception) {
                Log.e(TAG, "runCmd: ", e)
                _uiState.update {
                    it.copy(success = false)
                }
            }
        }
        _uiState.update {
            it.copy(
                running = false,
                completed = true,
            )
        }
    }

    fun reboot() {
        ShellUtils.reboot()
    }
}

@Stable
data class ShellScreenUiState(
    val output: ImmutableList<String> = persistentListOf(),
    val running: Boolean = false,
    val completed: Boolean = false,
    val success: Boolean = false,
)
