package com.ammar.quickswitchmanager.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val idMapRegex = "^IDMAP OF (.+)$".toRegex()
private val targetPathRegex = "^ +target path +: (.+)$".toRegex()
private val overlayPathRegex = "^ +overlay path +: (.+)$".toRegex()
private val mappingRegex = "^ +0x(?:\\d|[a-f])+ -> 0x(?:\\d|[a-f])+ \\((.+/.+) -> .+/.+\\)"
    .toRegex()

suspend fun getOverlayIDMaps() = withContext(Dispatchers.IO) {
    val result = ShellUtils.run("cmd overlay dump IDMAP")
    if (!result.isSuccess) {
        throw ShellException(result.err)
    }
    val out = result.out
    val idMaps = mutableListOf<IDMap>()
    out.forEach {
        val packageMatch = idMapRegex.matchEntire(it)
        if (packageMatch != null) {
            val packageName = packageMatch.groups[1] ?: return@forEach
            idMaps.add(
                IDMap(
                    packageName = packageName.value,
                ),
            )
            return@forEach
        }

        val lastMap = idMaps.lastOrNull() ?: return@forEach

        val targetPathMatch = targetPathRegex.matchEntire(it)
        if (targetPathMatch != null) {
            val targetPath = targetPathMatch.groups[1] ?: return@forEach
            replaceLastMap(
                idMaps = idMaps,
                replaceWith = lastMap.copy(
                    targetPath = targetPath.value,
                ),
            )
            return@forEach
        }

        val overlayPathMatch = overlayPathRegex.matchEntire(it)
        if (overlayPathMatch != null) {
            val overlayPath = overlayPathMatch.groups[1] ?: return@forEach
            replaceLastMap(
                idMaps = idMaps,
                replaceWith = lastMap.copy(
                    overlayPath = overlayPath.value,
                ),
            )
            return@forEach
        }

        val mappingMatch = mappingRegex.matchEntire(it)
        if (mappingMatch != null) {
            val mapping = mappingMatch.groups[1] ?: return@forEach
            val prevResources = lastMap.resources
            replaceLastMap(
                idMaps = idMaps,
                replaceWith = lastMap.copy(
                    resources = prevResources + mapping.value,
                ),
            )
            return@forEach
        }
    }
    idMaps.toList()
}

private fun replaceLastMap(
    idMaps: MutableList<IDMap>,
    replaceWith: IDMap,
) {
    idMaps.removeAt(idMaps.size - 1)
    idMaps.add(replaceWith)
}

data class IDMap(
    val packageName: String,
    val targetPath: String? = null,
    val overlayPath: String? = null,
    val resources: Set<String> = emptySet(),
)

fun filterRecentCompIDMaps(idMaps: List<IDMap>) = idMaps.filter {
    it.resources.contains(OVERLAY_RECENTS_CONFIG_NAME)
}

suspend fun getCurrentRecentsProviderPackageName(idMapToLookup: IDMap): String? {
    var currentRecentsProviderPackageName: String? = null
    val result = overlayLookup(
        packageName = idMapToLookup.packageName,
        field = OVERLAY_RECENTS_CONFIG_NAME,
    )
    if (!result.isSuccess) {
        throw ShellException(result.err)
    }
    val out = result.out
    if (out.isNotEmpty()) {
        out.first().split("/").getOrNull(0)?.let {
            currentRecentsProviderPackageName = it
        }
    }
    return currentRecentsProviderPackageName
}

suspend fun overlayLookup(
    packageName: String,
    field: String,
) = withContext(Dispatchers.Default) {
    ShellUtils.run("cmd overlay lookup $packageName $packageName:$field")
}
