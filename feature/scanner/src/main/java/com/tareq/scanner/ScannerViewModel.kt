package com.tareq.scanner


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
                handleArchive(
                    isArchived = uiState.value.wifiFields.isArchived,
                    deleteAction = { deleteWifiFromDatabase(uiState.value.wifiFields.ssid) },
                    insertAction = {
                        insertWifiIntoDatabase(
                            uiState.value.wifiFields,
                            uiState.value.scanDate
                        )
                    }
                )
            }

            ScanItemCategory.EMAIL -> {
                handleArchive(
                    isArchived = uiState.value.emailFields.isArchived,
                    deleteAction = { deleteEmailFromDatabase(uiState.value.emailFields.email) },
                    insertAction = {
                        insertEmailIntoDatabase(
                            uiState.value.emailFields,
                            uiState.value.scanDate
                        )
                    }
                )
            }

            ScanItemCategory.PRODUCT -> {
                handleArchive(
                    isArchived = uiState.value.productFields.isArchived,
                    deleteAction = { deleteProductFromDatabase(uiState.value.productFields.barcode) },
                    insertAction = {
                        insertProductIntoDatabase(
                            uiState.value.productFields,
                            uiState.value.scanDate
                        )
                    }
                )
            }

            ScanItemCategory.CONTACT_INFO -> {
                handleArchive(
                    isArchived = uiState.value.contactFields.isArchived,
                    deleteAction = { deleteContactFromDatabase(uiState.value.contactFields.name) },
                    insertAction = {
                        insertContactIntoDatabase(
                            uiState.value.contactFields,
                            uiState.value.scanDate
                        )
                    }
                )
            }
        }
    }
    //endregion

    //region email
    private fun updateEmailFields(barcode: Barcode) {
        barcode.email?.let { email ->
            _uiState.update {
                it.copy(
                    scanItemCategory = ScanItemCategory.EMAIL,
                    emailFields = EmailFields(
                        body = email.body.toString(),
                        subject = email.subject.toString(),
                        email = email.address.toString()
                    ),
                    isLoading = false,
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun insertEmailIntoDatabase(email: EmailFields, scanDate: String) {
        performDatabaseOperation(
            databaseOperation = {
                insertEmailIntoDatabaseUseCase(
                    email.toEmail(scanDate),
                    scanDate
                )
            },
            updateUiState = { updateEmailFields(!email.isArchived) },
            isArchived = !email.isArchived
        )
    }

    private fun deleteEmailFromDatabase(email: String) {
        performDatabaseOperation(
            databaseOperation = { deleteEmailFromDatabaseUseCase(email) },
            updateUiState = { updateEmailFields(false) },
            isArchived = false
        )
    }

    private fun updateEmailFields(isArchived: Boolean) {
        _uiState.update {
            it.copy(emailFields = it.emailFields.copy(isArchived = isArchived))
        }
    }
    //endregion

    //region contact
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
                    ),
                    isLoading = false,
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun insertContactIntoDatabase(contactFields: ContactFields, scanDate: String) {
        performDatabaseOperation(
            databaseOperation = {
                insertContactIntoDatabaseUseCase(
                    contactFields.toContact(scanDate),
                    scanDate
                )
            },
            updateUiState = { updateContactFields(!contactFields.isArchived) },
            isArchived = !contactFields.isArchived
        )
    }

    private fun deleteContactFromDatabase(contactName: String) {
        performDatabaseOperation(
            databaseOperation = { deleteContactFromDatabaseUseCase(contactName) },
            updateUiState = { updateContactFields(false) },
            isArchived = false
        )
    }

    private fun updateContactFields(isArchived: Boolean) {
        _uiState.update {
            it.copy(contactFields = it.contactFields.copy(isArchived = isArchived))
        }
    }
    //endregion

    // region wifi
    private fun deleteWifiFromDatabase(ssid: String) {
        performDatabaseOperation(
            databaseOperation = { deleteWifiFromDatabaseUseCase(ssid) },
            updateUiState = { updateWifiFields(false) },
            isArchived = false
        )
    }

    private fun insertWifiIntoDatabase(wifiFields: WifiFields, scanDate: String) {
        performDatabaseOperation(
            databaseOperation = { insertWifiIntoDatabaseUseCase(wifiFields.toWifi(scanDate)) },
            updateUiState = { updateWifiFields(!wifiFields.isArchived) },
            isArchived = !wifiFields.isArchived
        )
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
                    ),
                    isLoading = false,
                )
            }
        } ?: showToastMessage(R.string.unsupported_QrBr_code)
    }

    private fun updateWifiFields(isArchived: Boolean) {
        _uiState.update {
            it.copy(wifiFields = it.wifiFields.copy(isArchived = isArchived))
        }
    }
    //endregion

    //region product
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

    private fun deleteProductFromDatabase(productBarcode: String) {
        performDatabaseOperation(
            databaseOperation = { deleteProductFromDatabaseUseCase(productBarcode) },
            updateUiState = { updateProductArchiveState(false) },
            isArchived = false
        )
    }

    private fun insertProductIntoDatabase(productFields: ProductFields, scanDate: String) {
        performDatabaseOperation(
            databaseOperation = {
                insertProductIntoDatabaseUseCase(
                    productFields.toProduct(scanDate),
                    scanDate
                )
            },
            updateUiState = { updateProductArchiveState(!productFields.isArchived) },
            isArchived = productFields.isArchived
        )
    }

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

    private fun updateProductArchiveState(isArchived: Boolean) {
        _uiState.update {
            it.copy(productFields = it.productFields.copy(isArchived = isArchived))
        }
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

    private fun performDatabaseOperation(
        databaseOperation: suspend () -> DatabaseOperation,
        updateUiState: () -> Unit,
        isArchived: Boolean,
    ) {
        viewModelScope.launch {
            when (databaseOperation()) {
                is DatabaseOperation.InComplete -> {
                    if (isArchived)
                        showToastMessage(R.string.item_archive_failed)
                    else
                        showToastMessage(R.string.item_unarchive_failed)
                }

                DatabaseOperation.Complete -> {
                    updateUiState()
                    if (isArchived)
                        showToastMessage(R.string.item_archive_success)
                    else
                        showToastMessage(R.string.item_unarchive_success)
                }
            }
        }
    }

    private fun showToastMessage(messageFile: Int) {
        _uiState.update {
            it.copy(
                errorMessageFile = messageFile,
                isError = true,
                isLoading = false
            )
        }
        viewModelScope.launch {
            _effect.emit(ScannerEffect.ShowToastMessage(messageFile))
        }
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

    private fun handleArchive(
        isArchived: Boolean,
        deleteAction: () -> Unit,
        insertAction: () -> Unit
    ) {
        if (isArchived) deleteAction() else insertAction()
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
    //endregion
}