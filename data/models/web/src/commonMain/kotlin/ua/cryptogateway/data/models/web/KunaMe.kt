package ua.cryptogateway.data.models.web

import kotlinx.serialization.Serializable

@Serializable
data class KunaMe(
    val id: String,
    val fullName: String,
    val language: String,
)