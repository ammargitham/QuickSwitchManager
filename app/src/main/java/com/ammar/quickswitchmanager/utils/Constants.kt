package com.ammar.quickswitchmanager.utils

import com.topjohnwu.superuser.Shell

const val QS_OVERLAY_PACKAGE_NAME = "xyz.paphonb.quickswitch.overlay"
const val OVERLAY_RECENTS_CONFIG_NAME = "string/config_recentsComponentName"
const val QS_MODULE_DIR = "/data/adb/modules/quickswitch"
const val QS = "quickswitch"

val hasRoot by lazy { Shell.isAppGrantedRoot() == true }
