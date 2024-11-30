package ua.cryptogateway.data.service

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.cryptogateway.data.models.db.OrdersGridEntity
import ua.cryptogateway.data.schema.OrdersGridSchema
import ua.cryptogateway.data.schema.OrdersGridSchema.deleteord
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrdersGridDao(
    dispatchers: AppCoroutineDispatchers,
    private val database: Database,
) {
    private val dispatcher = dispatchers.io

    init {
        // We don't need to create table as we connect to database with already created schema
        //transaction(database) {
        //    SchemaUtils.create(OrdersGridSchema)
        //}
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = dispatcher, db = database) { block() }

    suspend fun readAll(): List<OrdersGridEntity> {
        return dbQuery {
            OrdersGridSchema.selectAll()
                //.where { Users.id eq id }
                .map { row ->
                    OrdersGridEntity(
                        pairname = row[OrdersGridSchema.pairname],
                        side = row[OrdersGridSchema.side],
                        remvolume = row[OrdersGridSchema.remvolume],
                        volume = row[OrdersGridSchema.volume],
                        pricebid = row[OrdersGridSchema.pricebid],
                        price = row[OrdersGridSchema.price],
                        priceask = row[OrdersGridSchema.priceask],
                        change = row[OrdersGridSchema.change],
                        gecko = row[OrdersGridSchema.gecko],
                        offset = row[OrdersGridSchema.offset],
                        smashort = row[OrdersGridSchema.smashort],
                        smalong = row[OrdersGridSchema.smalong],
                        atr = row[OrdersGridSchema.atr],
                        id = row[OrdersGridSchema.id],
                        status = row[OrdersGridSchema.status],
                        ordtype = row[OrdersGridSchema.ordtype],
                        deleteord = row[OrdersGridSchema.deleteord],
                        timemarket = row[OrdersGridSchema.timemarket],
                        lost = row[OrdersGridSchema.lost]
                    )
                }
        }
    }

}