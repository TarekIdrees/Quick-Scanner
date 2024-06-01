package com.tareq.feature.archive

import androidx.annotation.DrawableRes
import com.tareq.core.design.system.R
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.tareq.model.Wifi
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ArchiveUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val wifiArchiveItems: List<WifiArchiveItem> = persistentListOf(),
)

@Immutable
data class WifiArchiveItem(
    @DrawableRes val iconFile: Int = R.drawable.ic_wifi,
    val ssid: String = "",
    val password: String = "",
    val encryptionType: String = "",
    val scanDate: String = "",
)


fun Wifi.toWifiArchiveItem() = WifiArchiveItem(
    ssid = ssid,
    password = password,
    encryptionType = encryptionType,
    scanDate = scanDate
)

fun ArchiveUiState.isArchivedItemsEmpty() =
    wifiArchiveItems.isEmpty()