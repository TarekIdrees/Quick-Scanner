package com.tareq.feature.archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tareq.domain.Result
import com.tareq.domain.repository.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWifiArchiveItems()
    }

    private fun loadWifiArchiveItems() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            scannerRepository.getArchivedWifiItems()
                .flowOn(Dispatchers.IO)
                .collect {
                    when (it) {
                        is Result.Error -> _uiState.update { state -> state.copy(isError = true) }
                        is Result.Success -> {
                            Log.d("adnaad","${it.value}")
                            _uiState.update { state ->
                                state.copy(
                                    wifiArchiveItems = it.value.map { wifi -> wifi.toWifiArchiveItem() },
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }
}