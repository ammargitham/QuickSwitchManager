package com.ammar.quickswitchmanager.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.pm.ResolveInfo
import android.os.Build
import android.widget.Toast
import com.ammar.quickswitchmanager.utils.isRecentsProvider


fun Context.getAllLaunchers(): List<ResolveInfo> {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.queryIntentActivities(intent, ResolveInfoFlags.of(0))
    } else {
        packageManager.queryIntentActivities(intent, 0)
    }
}

fun Context.getCurrentLauncher(): ResolveInfo? {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    val flag = PackageManager.MATCH_DEFAULT_ONLY
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.resolveActivity(
            intent,
            ResolveInfoFlags.of(flag.toLong()),
        )
    } else {
        packageManager.resolveActivity(intent, flag)
    }
}

fun Context.getAllRecentsProviders(): List<PackageInfo> = getAllLaunchers()
    .mapNotNull {
        packageManager.getPackageInfo(
            it.activityInfo.packageName,
            PackageManager.GET_PERMISSIONS,
        )
    }
    .filter { isRecentsProvider(this, it) }

// fun Context.getInstalledPackages(flags: Int): List<PackageInfo> {
//     return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//         packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong()))
//     } else {
//         packageManager.getInstalledPackages(flags)
//     }
// }

fun Context.resolveServiceCompat(
    intent: Intent,
    flags: Int,
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    packageManager.resolveService(intent, ResolveInfoFlags.of(flags.toLong()))
} else {
    packageManager.resolveService(intent, flags)
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
