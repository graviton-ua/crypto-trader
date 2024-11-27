package ua.cryptogateway.data.web.api

import kotlinx.serialization.Serializable

/**
 * A wrapper data class representing a response from the Kuna API.
 *
 * @param T The type of the data contained in the response.
 * @property data The payload of the response.
 */
@Serializable
data class KunaResponse<T>(
    val data: T,
)