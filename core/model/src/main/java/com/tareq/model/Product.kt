package com.tareq.model

data class Product(
    val barcode: String,
    val title: String,
    val description: String,
    val brand: String,
    val manufacturer: String,
    val category: String,
    val images: List<String>,
    val ingredients: String,
    val size: String
)
