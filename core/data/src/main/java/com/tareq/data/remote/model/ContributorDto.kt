package com.tareq.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContributorDto(
    @SerialName("name")
    val name: String?,
    @SerialName("role")
    val role: String?
)