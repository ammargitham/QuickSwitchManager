package com.ammar.quickswitchmanager.common

import android.graphics.drawable.Drawable

data class PackageDetails(
    val packageName: String,
    val icon: Drawable?,
    val label: String,
    val isCurrentRecentsProvider: Boolean,
    val isCurrentHome: Boolean,
)
