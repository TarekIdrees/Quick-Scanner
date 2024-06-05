package com.tareq.data.remote.mapper

import com.tareq.data.remote.model.ProductDto
import com.tareq.model.Product

internal fun ProductDto.toProduct() = Product(
    barcode = barcodeNumber ?: "",
    title = title ?: "",
    description = description ?: "",
    brand = brand ?: "",
    manufacturer = manufacturer ?: "",
    category = category ?: "",
    images = images?.filterNotNull() ?: emptyList(),
    ingredients = ingredients ?: "",
    size = size ?: "",
    scanDate = ""
)