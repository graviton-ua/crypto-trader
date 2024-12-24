package ua.crypto.data.db.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant

internal object InstantTimestampColumnAdapter : ColumnAdapter<Instant, java.time.OffsetDateTime> {
    override fun decode(databaseValue: java.time.OffsetDateTime): Instant = databaseValue.toInstant().toKotlinInstant()

    override fun encode(value: Instant): java.time.OffsetDateTime {
        return java.time.OffsetDateTime.ofInstant(value.toJavaInstant(), java.time.ZoneOffset.UTC)
    }
}
