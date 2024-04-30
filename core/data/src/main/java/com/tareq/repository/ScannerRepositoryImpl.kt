package com.tareq.repository

import com.tareq.data.local.ScanItemsSource
import com.tareq.model.local.ScanItem
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(
    private val scanItemsSource: ScanItemsSource
) : ScannerRepository {
    override suspend fun getScanItems(): List<ScanItem> {
        return scanItemsSource.scanItems
    }
}