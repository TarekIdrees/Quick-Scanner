package com.tareq.data.local.mapper

import com.tareq.data.local.entity.ProductEntity
import com.tareq.model.Product

internal fun Product.toProductEntity() = ProductEntity(
    barcode = barcode,
    title = title,
    description = description,
    brand = brand,
    manufacturer = manufacturer,
    category = category,
    images = images,
    ingredients = ingredients,
    size = size,
)