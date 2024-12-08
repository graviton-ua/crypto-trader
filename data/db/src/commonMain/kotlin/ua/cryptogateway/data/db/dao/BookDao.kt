package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.BookAskEntity
import ua.cryptogateway.data.db.models.BookBidEntity
import ua.cryptogateway.data.db.schema.BookAskSchema
import ua.cryptogateway.data.db.schema.BookBidSchema
import ua.cryptogateway.util.AppCoroutineDispatchers


@Inject
class BookDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun getBookAsk(): List<BookAskEntity> = dbQuery {
        BookAskSchema.selectAll()
            .map { row ->
                BookAskEntity(
                    price = row[BookAskSchema.price],
                    volume = row[BookAskSchema.volume],
                    sumVolume = row[BookAskSchema.sumVolume]
                )
            }
    }

    suspend fun getBookBid(): List<BookBidEntity> = dbQuery {
        BookBidSchema.selectAll()
            .map { row ->
                BookBidEntity(
                    price = row[BookBidSchema.price],
                    volume = row[BookBidSchema.volume],
                    sumVolume = row[BookBidSchema.sumVolume]
                )
            }
    }

}