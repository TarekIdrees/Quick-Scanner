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
    private val getProductByBarcodeUseCase: GetProductByBarcodeUseCase
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
                    showToastMessage(R.string.unsupported_QrBr_code)
                }
            _uiState.update { it.copy(isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false) }
            showToastMessage(R.string.unsupported_QrBr_code)
        }
    }

    fun onClickBackArrow() {
        _uiState.update { it.copy(scanItemCategory = ScanItemCategory.EMPTY, isError = false) }
    }
    //endregion

    //region retrieve data
    private fun getProductByBarcode(barcode: Barcode) {
        _uiState.update { it.copy(isLoading = true) }
        barcode.displayValue?.let { barcodeNumber ->
            viewModelScope.launch(Dispatchers.IO) {
                when (val product = getProductByBarcodeUseCase(barcodeNumber)) {
                    is Result.Error -> {
                        handelErrorState(product.error)
                    }

                    is Result.Success -> {
                        updateProductFields(product.value)
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

    private fun handelErrorState(error: DataError) {
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