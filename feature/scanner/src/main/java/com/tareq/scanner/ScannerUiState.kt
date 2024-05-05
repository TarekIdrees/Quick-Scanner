package com.tareq.scanner

import androidx.compose.runtime.Stable
import com.tareq.model.local.ScanItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ScannerUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val barcode: String = "",
    val wifiFields: WifiFields = WifiFields(),
    val scanItemCategory: ScanItemCategory = ScanItemCategory.EMPTY,
    val scanItems: ImmutableList<ScanItem> = persistentListOf(),
    val scanDate: String = "",
)

data class WifiFields(
    val ssid: String = "",
    val password: String = "",
    val encryptionType: String = "",
)

enum class ScanItemCategory {
    EMPTY,
    WIFI,
    EMAIL,
    LOCATION,
    PRODUCT,
    URL,
    CONTACT,
    UN_SUPPORTED
}

fun ScannerUiState.isContentVisible() =
    !isLoading && !isError && scanItemCategory == ScanItemCategory.EMPTY
