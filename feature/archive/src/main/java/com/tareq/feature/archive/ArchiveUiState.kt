package com.tareq.feature.archive

import androidx.annotation.DrawableRes
import com.tareq.core.design.system.R
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.tareq.model.Contact
import com.tareq.model.Email
import com.tareq.model.Product
import com.tareq.model.Wifi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
data class ArchiveUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val wifiArchiveItems: ImmutableList<WifiArchiveItem> = persistentListOf(),
    val contactArchiveItems: ImmutableList<ContactArchiveItem> = persistentListOf(),
    val emailArchiveItems: ImmutableList<EmailArchiveItem> = persistentListOf(),
    val productArchiveItems: ImmutableList<ProductArchiveItem> = persistentListOf(),
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

@Immutable
data class ContactArchiveItem(
    @DrawableRes val iconFile: Int = R.drawable.ic_contact,
    val name: String = "",
    val title: String = "",
    val phoneNumbers: ImmutableList<String> = persistentListOf(),
    val emails: ImmutableList<String> = persistentListOf(),
    val addresses: ImmutableList<String> = persistentListOf(),
    val urls: ImmutableList<String> = persistentListOf(),
    val organization: String = "",
    val scanDate: String = "",
)

fun Contact.toContactArchiveItem() = ContactArchiveItem(
    name = name,
    title = title,
    phoneNumbers = phoneNumbers.toImmutableList(),
    emails = emails.toImmutableList(),
    addresses = addresses.toImmutableList(),
    urls = urls.toImmutableList(),
    organization = organization,
    scanDate = scanDate
)

@Immutable
data class EmailArchiveItem(
    val email: String = "",
    val subject: String = "",
    val body: String = "",
    val scanDate: String = "",
)

fun Email.toEmailArchiveItem() = EmailArchiveItem(
    email = email,
    subject = subject,
    body = body,
    scanDate = scanDate
)

@Immutable
data class ProductArchiveItem(
    val barcode: String = "",
    val title: String = "",
    val description: String = "",
    val brand: String = "",
    val manufacturer: String = "",
    val category: String = "",
    val images: ImmutableList<String> = persistentListOf(),
    val ingredients: String = "",
    val size: String = "",
    val scanDate: String = "",
)

fun Product.toProductArchiveItem() = ProductArchiveItem(
    barcode = barcode,
    title = title,
    description = description,
    brand = brand,
    manufacturer = manufacturer,
    category = category,
    images = images.toImmutableList(),
    ingredients = ingredients,
    size = size,
    scanDate = scanDate
)

fun ArchiveUiState.isArchivedItemsEmpty() =
    wifiArchiveItems.isEmpty() && contactArchiveItems.isEmpty() && emailArchiveItems.isEmpty()