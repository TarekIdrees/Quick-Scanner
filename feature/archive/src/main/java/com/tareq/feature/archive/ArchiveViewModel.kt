package com.tareq.feature.archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tareq.domain.DataError
import com.tareq.domain.Result
import com.tareq.domain.usecase.GetArchivedContactsUseCase
import com.tareq.domain.usecase.GetArchivedEmailsUseCase
import com.tareq.domain.usecase.GetArchivedWifiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val getArchivedWifiUseCase: GetArchivedWifiUseCase,
    private val getArchivedContactsUseCase: GetArchivedContactsUseCase,
    private val getArchivedEmailsUseCase: GetArchivedEmailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadArchivedWifi()
        loadArchivedContacts()
        loadArchivedEmails()
    }

    private fun loadArchivedWifi() {
        loadArchivedItems(
            flow = getArchivedWifiUseCase(),
            transform = { it.toWifiArchiveItem() },
            updateState = { items ->
                _uiState.update { it.copy(wifiArchiveItems = items) }
            }
        )
    }

    private fun loadArchivedContacts() {
        loadArchivedItems(
            flow = getArchivedContactsUseCase(),
            transform = { it.toContactArchiveItem() },
            updateState = { items ->
                _uiState.update { it.copy(contactArchiveItems = items) }
            }
        )
    }

    private fun loadArchivedEmails() {
        loadArchivedItems(
            flow = getArchivedEmailsUseCase(),
            transform = { it.toEmailArchiveItem() },
            updateState = { items ->
                _uiState.update { it.copy(emailArchiveItems = items) }
            }
        )
    }

    private inline fun <reified DomainModel, reified UiModel> ViewModel.loadArchivedItems(
        flow: Flow<Result<List<DomainModel>, DataError.Local>>,
        crossinline transform: (DomainModel) -> UiModel,
        crossinline updateState: (List<UiModel>) -> Unit
    ) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            flow.flowOn(Dispatchers.IO).collect { result ->
                when (result) {
                    is Result.Error -> _uiState.update { it.copy(isError = true) }
                    is Result.Success -> {
                        Log.d("Adnaan", "loadArchivedItems: ${result.value}")
                        val transformedItems = result.value.map { transform(it) }
                        updateState(transformedItems)
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}