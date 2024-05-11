package com.tareq.scanner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.tareq.domain.GetScanItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
    private val getScanItemsUseCase: GetScanItemsUseCase
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
                            _uiState.update { it.copy(barcode = barcode.displayValue ?: "") }
                        }

                        Barcode.TYPE_CONTACT_INFO -> {
                            updateContactFields(barcode)
                        }

                        else -> {
                            showUnsupportedBarcodeMessage()
                        }
                    }
                }
                .addOnCanceledListener {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isError = true, isLoading = false) }
                    showUnsupportedBarcodeMessage()
                }
            _uiState.update { it.copy(isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, isError = true) }
            showUnsupportedBarcodeMessage()
        }
    }

    fun onClickBackArrow() {
        _uiState.update { it.copy(scanItemCategory = ScanItemCategory.EMPTY) }
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
        } ?: showUnsupportedBarcodeMessage()
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
        } ?: showUnsupportedBarcodeMessage()
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
        } ?: showUnsupportedBarcodeMessage()
    }

    private fun executeBrowserLinkBarcode(barcode: Barcode) {
        val url = barcode.url?.url
        run {
            if (url != null) {
                viewModelScope.launch {
                    _effect.emit(ScannerEffect.OpenUrl(url))
                }
            } else {
                showUnsupportedBarcodeMessage()
            }
        }
    }

    private fun showUnsupportedBarcodeMessage() {
        viewModelScope.launch {
            _effect.emit(ScannerEffect.ShowUnSupportedQBcodeMessage)
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
}