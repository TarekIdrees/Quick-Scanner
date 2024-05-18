package com.tareq.data.remote

import com.tareq.data.remote.model.ProductDto

interface ProductApi {
    suspend fun getProductByBarcode(barcode: String): ApiCallResult<ProductDto>
}