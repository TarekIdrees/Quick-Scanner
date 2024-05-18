package com.tareq.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("age_group")
    val ageGroup: String?,
    @SerialName("asin")
    val asin: String?,
    @SerialName("barcode_formats")
    val barcodeFormats: String?,
    @SerialName("barcode_number")
    val barcodeNumber: String?,
    @SerialName("brand")
    val brand: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("color")
    val color: String?,
    @SerialName("contributors")
    val contributorsDto: List<ContributorDto?>?,
    @SerialName("description")
    val description: String?,
    @SerialName("energy_efficiency_class")
    val energyEfficiencyClass: String?,
    @SerialName("format")
    val format: String?,
    @SerialName("gender")
    val gender: String?,
    @SerialName("height")
    val height: String?,
    @SerialName("images")
    val images: List<String?>?,
    @SerialName("ingredients")
    val ingredients: String?,
    @SerialName("last_update")
    val lastUpdate: String?,
    @SerialName("length")
    val length: String?,
    @SerialName("manufacturer")
    val manufacturer: String?,
    @SerialName("material")
    val material: String?,
    @SerialName("model")
    val model: String?,
    @SerialName("mpn")
    val mpn: String?,
    @SerialName("multipack")
    val multipack: String?,
    @SerialName("nutrition_facts")
    val nutritionFacts: String?,
    @SerialName("pattern")
    val pattern: String?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("size")
    val size: String?,
    @SerialName("stores")
    val storesDto: List<StoreDto?>?,
    @SerialName("title")
    val title: String?,
    @SerialName("weight")
    val weight: String?,
    @SerialName("width")
    val width: String?
)