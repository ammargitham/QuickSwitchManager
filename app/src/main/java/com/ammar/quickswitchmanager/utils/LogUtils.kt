package com.ammar.quickswitchmanager.utils

import android.content.Context
import android.net.Uri
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.time.Instant
import java.time.format.DateTimeFormatter


object LogUtils {
    val logFileName: String
        get() = "QuickSwitch-Logs-${DateTimeFormatter.ISO_INSTANT.format(Instant.now())}.log"

    fun writeToUri(
        context: Context,
        uri: Uri,
        logs: List<String>,
    ) {
        context.contentResolver.openOutputStream(uri)?.use { out ->
            BufferedWriter(OutputStreamWriter(out)).use {
                it.write(logs.joinToString("\n"))
            }
        }
    }
}
