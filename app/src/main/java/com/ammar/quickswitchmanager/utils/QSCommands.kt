package com.ammar.quickswitchmanager.utils

object QSCommands {
    fun reset() = cli("reset")

    fun uninstall() = cli("uninstall")

    fun grid() = cli("grid")

    fun ch(packageName: String) = cli("ch", packageName)

    private fun cli(arg1: String, arg2: String? = null): String {
        var str = "$QS_MODULE_DIR/$QS --$arg1"
        if (arg2 != null) {
            str += "=$arg2"
        }
        return str
    }
}
