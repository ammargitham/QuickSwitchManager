package com.ammar.quickswitchmanager.utils

import com.topjohnwu.superuser.CallbackList
import com.topjohnwu.superuser.Shell
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.withContext

object ShellUtils {
    suspend fun run(vararg commands: String) = withContext(coroutineContext) {
        val shell = Shell.getShell()
        if (!shell.isRoot) {
            throw NoRootException()
        }
        Shell.cmd(*commands).exec()
    }

    suspend fun run(
        vararg commands: String,
        callback: (String) -> Unit,
    ) = withContext(coroutineContext) {
        val shell = Shell.getShell()
        if (!shell.isRoot) {
            throw NoRootException()
        }
        Shell.cmd(*commands).to(
            object : CallbackList<String?>() {
                override fun onAddElement(s: String?) {
                    callback(s ?: return)
                }
            },
        ).exec()
    }


    fun reboot(reason: String = "") {
        Shell.cmd("/system/bin/svc power reboot $reason || /system/bin/reboot $reason").submit()
    }
}

open class ShellException : Exception {
    constructor(msg: String) : super(msg)
    constructor(err: List<String>) : super(err.joinToString("\n"))
}

class NoRootException : ShellException("Root not granted")
