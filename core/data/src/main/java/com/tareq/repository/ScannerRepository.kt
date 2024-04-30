package com.tareq.repository

import com.tareq.model.local.ScanItem

interface ScannerRepository {
    suspend fun getScanItems(): List<ScanItem>
}