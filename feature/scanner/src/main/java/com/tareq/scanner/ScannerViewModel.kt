package com.tareq.scanner

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.tareq.domain.GetScanItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanner: GmsBarcodeScanner,
    private val getScanItemsUseCase: GetScanItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadScanItems()
    }

    fun onClickScanButton() {
        try {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    when (barcode.valueType) {
                        Barcode.TYPE_EMAIL -> {
                            Log.d("Tarek", "${barcode.email!!.body}")
                        }

                        Barcode.TYPE_WIFI -> {
                            Log.d("Tarek", "${barcode.wifi!!.password}")
                            Log.d("Tarek", "${barcode.wifi!!.ssid}")
                            Log.d("Tarek", "${barcode.wifi!!.encryptionType}")
                        }

                        Barcode.TYPE_PRODUCT -> {
                            _uiState.update { it.copy(barcode = barcode.displayValue ?: "") }
                        }

                        else -> {
                            Log.d("Tarek", "${barcode.format}")
                            Log.d("Tarek", "${barcode.valueType}")
                        }
                    }
                }
                .addOnCanceledListener {

                }
                .addOnFailureListener {
                    Log.d("Tarek", "failed")
                }
        } catch (e: Exception) {
            Log.d("Tarek", "catch error $e")
        }
    }

    private fun loadScanItems() {
        viewModelScope.launch {
            val scanItems = getScanItemsUseCase().toImmutableList()
            _uiState.update { it.copy(scanItems = scanItems) }
        }
    }
}