package ua.cryptogateway.data.web.api

import kotlinx.serialization.Serializable

@Serializable
data class KunaResponse<T>(
    val data: T,
)