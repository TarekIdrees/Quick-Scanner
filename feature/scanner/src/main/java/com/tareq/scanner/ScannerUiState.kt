package com.tareq.scanner

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.tareq.model.Product
import com.tareq.model.Wifi
import com.tareq.model.local.ScanItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ScannerUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    @StringRes val errorMessageFile: Int = 0,
    val barcode: String = "",
    val wifiFields: WifiFields = WifiFields(),
    val contactFields: ContactFields = ContactFields(),
    val emailFields: EmailFields = EmailFields(),
    val productFields: ProductFields = ProductFields(),
    val scanItemCategory: ScanItemCategory = ScanItemCategory.EMPTY,
    val scanItems: ImmutableList<ScanItem> = persistentListOf(),
    val scanDate: String = "",
)

@Immutable
data class WifiFields(
    val ssid: String = "",
    val password: String = "",
    val encryptionType: String = "",
    val isArchived: Boolean = false,
)

fun WifiFields.toWifi() = Wifi(
    ssid = ssid,
    password = password,
    encryptionType = encryptionType
)

@Immutable
data class ContactFields(
    val name: String = "",
    val title: String = "",
    val phoneNumbers: ImmutableList<String> = persistentListOf(),
    val emails: ImmutableList<String> = persistentListOf(),
    val addresses: ImmutableList<String> = persistentListOf(),
    val urls: ImmutableList<String> = persistentListOf(),
    val organization: String = "",
)

@Immutable
data class EmailFields(
    val email: String = "",
    val subject: String = "",
    val body: String = "",
)

@Immutable
data class ProductFields(
    val barcode: String = "",
    val title: String = "",
    val description: String = "",
    val brand: String = "",
    val manufacturer: String = "",
    val category: String = "",
    val images: ImmutableList<String> = persistentListOf(),
    val ingredients: String = "",
    val size: String = "",
    val isArchived: Boolean = false,
)

fun ProductFields.toProduct() = Product(
    barcode = barcode,
    title = title,
    description = description,
    brand = brand,
    manufacturer = manufacturer,
    category = category,
    images = images,
    ingredients = ingredients,
    size = size
)

@Immutable
enum class ScanItemCategory {
    EMPTY,
    WIFI,
    EMAIL,
    PRODUCT,
    CONTACT_INFO,
}

fun ScannerUiState.isContentVisible() =
    !isLoading && !isError && scanItemCategory == ScanItemCategory.EMPTY
