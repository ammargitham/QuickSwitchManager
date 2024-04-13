package com.ammar.quickswitchmanager.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

val MutableListOfStringSaver = Saver<MutableState<List<String>>, Array<String>>(
    save = { it.value.toTypedArray() },
    restore = { mutableStateOf(it.toList()) },
)
