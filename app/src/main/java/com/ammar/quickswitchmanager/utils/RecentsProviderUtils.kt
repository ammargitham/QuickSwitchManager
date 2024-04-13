package com.ammar.quickswitchmanager.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import com.ammar.quickswitchmanager.extensions.resolveServiceCompat


fun isRecentsProvider(
    context: Context,
    packageInfo: PackageInfo,
): Boolean {
    val packageName = packageInfo.packageName
    val serviceName = if (packageName != "com.android.systemui") {
        "android.intent.action.QUICKSTEP_SERVICE"
    } else {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            return true
        } else {
            "com.android.systemui.recents.TOGGLE_RECENTS"
        }
    }
    val intent = Intent(serviceName).setPackage(packageName)
    return context.resolveServiceCompat(intent, 0) != null
}

// fun isCurrentRecentProvider(packageInfo: PackageInfo): Boolean {
//     val requestedPermissions = packageInfo.requestedPermissions
//     val requestedPermissionsFlags = packageInfo.requestedPermissionsFlags
//     val i = requestedPermissions?.indexOfFirst { perm ->
//         perm == "android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS"
//     } ?: return false
//     return requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED ==
//         PackageInfo.REQUESTED_PERMISSION_GRANTED
// }
