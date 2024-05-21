package com.tareq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductEntity(
    val barcode: String,
    val title: String,
    val description: String,
    val brand: String,
    val manufacturer: String,
    val category: String,
    val images: List<String>,
    val ingredients: String,
    val size: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
