package com.tareq.domain.repository

import com.tareq.domain.DataError
import com.tareq.domain.Result
import com.tareq.model.Product
import com.tareq.model.local.ScanItem

interface ScannerRepository {
    suspend fun getScanItems(): List<ScanItem>

    suspend fun getProductByBarcode(barcode: String): Result<Product,DataError.Network>
}