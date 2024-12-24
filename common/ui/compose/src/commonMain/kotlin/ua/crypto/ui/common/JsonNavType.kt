package ua.crypto.ui.common

import androidx.core.bundle.Bundle
import androidx.navigation.NavType

abstract class JsonNavType<T>(override val isNullableAllowed: Boolean = false) : NavType<T>(isNullableAllowed = isNullableAllowed) {
    abstract fun fromJsonParse(value: String): T
    abstract fun T.getJsonParse(): String

    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(::parseValue)

    override fun parseValue(value: String): T = fromJsonParse(value)

    override fun serializeAsValue(value: T): String = value.getJsonParse()

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, value.getJsonParse())
    }
}