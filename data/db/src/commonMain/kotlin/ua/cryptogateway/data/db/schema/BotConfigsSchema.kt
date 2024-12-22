package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table
import ua.cryptogateway.data.db.columns.OrderSideColumnType

object BotConfigsSchema : Table("botconfigs") {
    val id = integer("id").autoIncrement()

    val baseAsset = varchar("baseAsset", 8)
    val quoteAsset = varchar("quoteAsset", 8)

    // Type or orders to create sell/buy
    val side = registerColumn("side", OrderSideColumnType())

    // Amount of base asset to safe on balance
    val fond = double("fond")

    // Start price from which first order should be created for sell/buy
    val startPrice = double("startPrice")
    val priceStep = double("priceStep")
    val biasPrice = double("biasPrice")
    val minSize = double("minSize")
    val orderSize = integer("orderSize")
    val sizeStep = double("sizeStep")
    val orderAmount = integer("orderAmount")
    val priceForce = bool("priceForce")
    val market = bool("market")

    val basePrec = integer("basePrec")
    val quotePrec = integer("quotePrec")
    val active = bool("active")

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(baseAsset, quoteAsset, side)
    }
}