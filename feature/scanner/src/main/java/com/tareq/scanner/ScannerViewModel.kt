package com.tareq.scanner


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.tareq.domain.DataError
import com.tareq.domain.Result
import com.tareq.domain.usecase.GetProductByBarcodeUseCase
import com.tareq.domain.usecase.GetScanItemsUseCase
import com.tareq.core.design.system.R
import com.tareq.domain.DatabaseOperation
import com.tareq.domain.usecase.DeleteContactFromDatabaseUseCase
import com.tareq.domain.usecase.DeleteEmailFromDatabaseUseCase
import com.tareq.domain.usecase.DeleteProductFromDatabaseUseCase
import com.tareq.domain.usecase.DeleteWifiFromDatabaseUseCase
import com.tareq.domain.usecase.InsertContactIntoDatabaseUseCase
import com.tareq.domain.usecase.InsertEmailIntoDatabaseUseCase
import com.tareq.domain.usecase.InsertProductIntoDatabaseUseCase
import com.tareq.domain.usecase.InsertWifiIntoDatabaseUseCase
import com.tareq.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanner: GmsBarcodeScanner,
    private val getScanItemsUseCase: GetScanItemsUseCase,
    private val getProductByBarcodeUseCase: GetProductByBarcodeUseCase,
    private val insertProductIntoDatabaseUseCase: InsertProductIntoDatabaseUseCase,
    private val deleteProductFromDatabaseUseCase: DeleteProductFromDatabaseUseCase,
    private val insertWifiIntoDatabaseUseCase: InsertWifiIntoDatabaseUseCase,
    private val deleteWifiFromDatabaseUseCase: DeleteWifiFromDatabaseUseCase,
    private val insertContactIntoDatabaseUseCase: InsertContactIntoDatabaseUseCase,
    private val deleteContactFromDatabaseUseCase: DeleteContactFromDatabaseUseCase,
    private val deleteEmailFromDatabaseUseCase: DeleteEmailFromDatabaseUseCase,
    private val insertEmailIntoDatabaseUseCase: InsertEmailIntoDatabaseUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ScannerEffect>()
    val effect = _effect.asSharedFlow()

    private val currentDate: String by lazy {
        LocalDate.now().toString()
    }

    init {
        loadScanItems()
    }

    private fun loadScanItems() {
        viewModelScope.launch {
            val scanItems = getScanItemsUseCase().toImmutableList()
            _uiState.update { it.copy(scanItems = scanItems) }
        }
    }

    //region clicks event
    fun onClickScanButton() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    _uiState.update { it.copy(scanDate = currentDate) }
                    when (barcode.valueType) {
                        Barcode.TYPE_EMAIL -> {
                            updateEmailFields(barcode)
                        }

                        Barcode.TYPE_WIFI -> {
                            updateWifiFields(barcode)
                        }

                        Barcode.TYPE_URL -> {
                            executeBrowserLinkBarcode(barcode)
                        }

                        Barcode.TYPE_PRODUCT -> {
                            getProductByBarcode(barcode)
                        }

                        Barcode.TYPE_CONTACT_INFO -> {
                            updateContactFields(barcode)
                        }

                        else -> {
                            showToastMessage(R.string.unsupported_QrBr_code)
                        }
                    }
                }
                .addOnCanceledListener {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isLoading = false) }
                    showToastMessage(R.string.scan_failed)
                }
            _uiState.update { it.copy(isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false) }
            showToastMessage(R.string.scan_failed)
        }
    }

    fun onClickBackArrow() {
        _uiState.update { it.copy(scanItemCategory = ScanItemCategory.EMPTY, isError = false) }
    }

    fun onClickArchive() {
        when (uiState.value.scanItemCategory) {
            ScanItemCategory.EMPTY -> {}
            ScanItemCategory.WIFI -> {
                if (uiState.value.wifiFields.isArchived)
                    deleteWifiFromDatabase(uiState.value.wifiFields.ssid)
                else
                    insertWifiIntoDatabase(uiState.value.wifiFields, uiState.value.scanDate)
            }

            ScanItemCategory.EMAIL -> {
                if (uiState.value.emailFields.isArchived)
                    deleteEmailFromDatabase(uiState.value.emailFields.email)
                else
                    insertEmailIntoDatabase(uiState.value.emailFields, uiState.value.scanDate)
            }

            ScanItemCategory.PRODUCT -> {
                if (uiState.value.productFields.isArchived)
                    deleteProductFromDatabase(productBarcode = uiState.value.productFields.barcode)
                else
                    insertProductIntoDatabase(uiState.value.productFields, uiState.value.scanDate)
            }

            ScanItemCategory.CONTACT_INFO -> {
                if (uiState.value.contactFields.isArchived)
                    deleteContactFromDatabase(contactName = uiState.value.contactFields.name)
                else
                    insertContactIntoDatabase(uiState.value.contactFields, uiState.value.scanDate)
            }
        }
    }

    private fun insertEmailIntoDatabase(email: EmailFields, scanDate: String) {
        viewModelScope.launch {
            when (insertEmailIntoDatabaseUseCase(email.toEmail(), scanDate)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_archive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            emailFields = it.emailFields.copy(isArchived = !uiState.value.emailFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_archive_success)
                }
            }
        }
    }

    private fun deleteEmailFromDatabase(email: String) {
        viewModelScope.launch {
            when (deleteEmailFromDatabaseUseCase(email)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_unarchive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            emailFields = it.emailFields.copy(isArchived = !uiState.value.emailFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_unarchive_success)
                }
            }
        }
    }

    private fun insertContactIntoDatabase(contactFields: ContactFields, scanDate: String) {
        viewModelScope.launch {
            when (insertContactIntoDatabaseUseCase(contactFields.toContact(), scanDate)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_archive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            contactFields = it.contactFields.copy(isArchived = !uiState.value.contactFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_archive_success)
                }
            }
        }
    }

    private fun deleteContactFromDatabase(contactName: String) {
        viewModelScope.launch {
            when (deleteContactFromDatabaseUseCase(name = contactName)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_unarchive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            contactFields = it.contactFields.copy(isArchived = !uiState.value.contactFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_unarchive_success)
                }
            }
        }
    }

    private fun insertWifiIntoDatabase(wifiFields: WifiFields, scanDate: String) {
        viewModelScope.launch {
            when (insertWifiIntoDatabaseUseCase(wifiFields.toWifi(), scanDate)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_archive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            wifiFields = it.wifiFields.copy(isArchived = !uiState.value.wifiFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_archive_success)
                }
            }
        }
    }

    private fun deleteWifiFromDatabase(ssid: String) {
        viewModelScope.launch {
            when (deleteWifiFromDatabaseUseCase(ssid)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_unarchive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            wifiFields = it.wifiFields.copy(isArchived = !uiState.value.wifiFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_unarchive_success)
                }
            }
        }
    }

    private fun deleteProductFromDatabase(productBarcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (deleteProductFromDatabaseUseCase(productId = productBarcode)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_unarchive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            productFields = it.productFields.copy(isArchived = !uiState.value.productFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_unarchive_success)
                }
            }
        }
    }

    private fun insertProductIntoDatabase(productFields: ProductFields, scanDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (insertProductIntoDatabaseUseCase(productFields.toProduct(), scanDate)) {
                is DatabaseOperation.InComplete -> showToastMessage(R.string.item_archive_failed)
                DatabaseOperation.Complete -> {
                    _uiState.update {
                        it.copy(
                            productFields = it.productFields.copy(isArchived = !uiState.value.productFields.isArchived)
                        )
                    }
                    showToastMessage(R.string.item_archive_success)
                }
            }
        }
    }
//endregion

    //region retrieve data
    private fun getProductByBarcode(barcode: Barcode) {
        _uiState.update { it.copy(isLoading = true) }
        barcode.displayValue?.let { barcodeNumber ->
            viewModelScope.launch(Dispatchers.IO) {
                when (val productResult = getProductByBarcodeUseCase(barcodeNumber)) {
                    is Result.Error -> {
                        handelErrorState(productResult.error)
                    }

                    is Result.Success -> {
                        updateProductFields(productResult.value)
                    }
                }
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun getEncryptionType(encryptionNumber: Int): String {
        return when (encryptionNumber) {
            2 -> "TKIP (WPA)"
            5 -> "WEP"
            4 -> "CCMP (WPA)"
            3 -> "AES"
            7 -> "NONE"
            8 -> "AUTO"
            else -> "Unknown"
        }
    }

//endregion

    // region update scan items
    private fun updateProductFields(product: Product) {
        _uiState.update {
            it.copy(
                scanItemCategory = ScanItemCategory.PRODUCT,
                productFields = ProductFields(
                    barcode = product.barcode,
                    title = product.title,
                    brand = product.brand,
                    category = product.category,
                    description = product.description,
                    manufacturer = product.manufacturer,
                    size = product.size,
                    images = product.images.toImmutableList(),
                    ingredients = product.ingredients
                ),
                isLoading = false
            )
        }
    }

    private fun updateEmailFields(barcode: Barcode) {
        barcode.email?.let { email ->
            _uiState.update {
                it.copy(
                    scanItemCategory = ScanItemCategory.EMAIL,
                    emailFields = EmailFields(
                        body = email.body.toString(),
                        subject = email.subject.toString(),
                        email = email.address.toString()
                    )
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun handelErrorState(error: DataError.Network) {
        _uiState.update { it.copy(isError = true, isLoading = false) }
        when (error) {
            DataError.Network.NO_INTERNET -> showToastMessage(R.string.no_internet_connection)
            DataError.Network.FORBIDDEN -> showToastMessage(R.string.forbidden)
            DataError.Network.UNAUTHORIZED -> showToastMessage(R.string.unauthorized)
            DataError.Network.SERVER_ERROR -> showToastMessage(R.string.server_error)
            DataError.Network.SERIALIZATION_ERROR -> showToastMessage(R.string.serialization_error)
            DataError.Network.UNKNOWN -> showToastMessage(R.string.unknown_error)
            DataError.Network.EXCEED_LIMIT -> showToastMessage(R.string.exceed_limit)
            DataError.Network.NOT_FOUND -> showToastMessage(R.string.not_found)
        }
    }

    private fun updateWifiFields(barcode: Barcode) {
        barcode.wifi?.let { wifi ->
            _uiState.update {
                it.copy(
                    scanItemCategory = ScanItemCategory.WIFI,
                    wifiFields = WifiFields(
                        ssid = wifi.ssid.toString(),
                        password = wifi.password.toString(),
                        encryptionType = getEncryptionType(wifi.encryptionType)
                    )
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun updateContactFields(barcode: Barcode) {
        barcode.contactInfo?.let { contactInfo ->
            _uiState.update {
                it.copy(
                    scanItemCategory = ScanItemCategory.CONTACT_INFO,
                    contactFields = ContactFields(
                        name = contactInfo.name?.formattedName ?: "",
                        title = contactInfo.title ?: "",
                        phoneNumbers = contactInfo.phones.mapNotNull { phone -> phone.number }
                            .toImmutableList(),
                        emails = contactInfo.emails.mapNotNull { email -> email.address }
                            .toImmutableList(),
                        addresses = contactInfo.addresses.mapNotNull { address ->
                            address.addressLines.joinToString(", ")
                        }.toImmutableList(),
                        organization = contactInfo.organization ?: "",
                        urls = contactInfo.urls.mapNotNull { url -> url }.toImmutableList()
                    )
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

//endregion

    //region helper function
    private fun executeBrowserLinkBarcode(barcode: Barcode) {
        val url = barcode.url?.url
        run {
            if (url != null) {
                viewModelScope.launch {
                    _effect.emit(ScannerEffect.OpenUrl(url))
                }
            } else {
                showToastMessage(R.string.unsupported_QrBr_code)
            }
        }
    }

    private fun showToastMessage(messageFile: Int) {
        _uiState.update { it.copy(errorMessageFile = messageFile, isError = true) }
        viewModelScope.launch {
            _effect.emit(ScannerEffect.ShowToastMessage(messageFile))
        }
    }

//endregion


}