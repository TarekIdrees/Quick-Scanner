package com.tareq.repository

import com.tareq.data.local.ScanItemsSource
import com.tareq.data.remote.ApiCallResult
import com.tareq.data.remote.ProductApi
import com.tareq.data.remote.mapper.toProduct
import com.tareq.domain.DataError
import com.tareq.domain.Result
import com.tareq.domain.repository.ScannerRepository
import com.tareq.model.Product
import com.tareq.model.local.ScanItem
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(
    private val scanItemsSource: ScanItemsSource,
    private val productApi: ProductApi
) : ScannerRepository {
    override suspend fun getScanItems(): List<ScanItem> {
        return scanItemsSource.scanItems
    }

    override suspend fun getProductByBarcode(barcode: String): Result<Product,DataError.Network> {
        return when (val result = productApi.getProductByBarcode(barcode)) {
            is ApiCallResult.Success -> Result.Success(result.data.toProduct())
            is ApiCallResult.Failure -> Result.Error(result.exception)
        }
    }
}