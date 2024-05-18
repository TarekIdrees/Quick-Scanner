package com.tareq.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsDto(
    @SerialName("products")
    val products: List<ProductDto?>?
)