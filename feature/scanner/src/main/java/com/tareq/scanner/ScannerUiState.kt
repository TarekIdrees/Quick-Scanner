package com.tareq.scanner

import androidx.compose.runtime.Stable
import com.tareq.model.local.ScanItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ScannerUiState(
    val barcode: String = "",
    val scanItems: ImmutableList<ScanItem> = persistentListOf(),
)
