package com.tareq.scanner

import androidx.compose.runtime.Immutable
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
    val contactFields: ContactFields = ContactFields(),
    val scanItemCategory: ScanItemCategory = ScanItemCategory.EMPTY,
    val scanItems: ImmutableList<ScanItem> = persistentListOf(),
    val scanDate: String = "",
)

@Immutable
data class WifiFields(
    val ssid: String = "",
    val password: String = "",
    val encryptionType: String = "",
)

@Immutable
data class ContactFields(
    val name: String = "",
    val title: String = "",
    val phoneNumbers:ImmutableList<String> = persistentListOf(),
    val emails: ImmutableList<String> = persistentListOf(),
    val addresses: ImmutableList<String> = persistentListOf(),
    val urls: ImmutableList<String> = persistentListOf(),
    val organization: String = "",
)

@Immutable
enum class ScanItemCategory {
    EMPTY,
    WIFI,
    EMAIL,
    LOCATION,
    PRODUCT,
    URL,
    CONTACT_INFO,
    UN_SUPPORTED
}

fun ScannerUiState.isContentVisible() =
    !isLoading && !isError && scanItemCategory == ScanItemCategory.EMPTY
