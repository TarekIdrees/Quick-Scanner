package com.tareq.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreDto(
    @SerialName("availability")
    val availability: String?,
    @SerialName("condition")
    val condition: String?,
    @SerialName("country")
    val country: String?,
    @SerialName("currency")
    val currency: String?,
    @SerialName("currency_symbol")
    val currencySymbol: String?,
    @SerialName("item_group_id")
    val itemGroupId: String?,
    @SerialName("last_update")
    val lastUpdate: String?,
    @SerialName("link")
    val link: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("price")
    val price: String?,
    @SerialName("sale_price")
    val salePrice: String?,
)