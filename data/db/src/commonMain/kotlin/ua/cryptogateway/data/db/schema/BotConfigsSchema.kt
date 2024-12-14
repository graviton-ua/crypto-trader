package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table
import ua.cryptogateway.data.db.columns.SideColumnType

object BotConfigsSchema : Table("dbo.botconfigs") {
    val baseAsset = varchar("baseAsset", 8)
    val quoteAsset = varchar("quoteAsset", 8)

    // Type or orders to create sell/buy
    val side = registerColumn("side", SideColumnType())

    // Amount of base asset to buy or sell for orders
    val fond = double("fond")

    // Start price from which first order should be created for sell/buy
    val startPrice = double("startPrice")
    val priceStep = double("priceStep")
    val minSize = double("minSize")
    val orderSize = integer("orderSize")
    val orderAmount = integer("orderAmount")
    val priceForce = bool("priceForce")
    val market = bool("market")

    val basePrec = integer("basePrec")
    val quotePrec = integer("quotePrec")
    val active = bool("active")

    override val primaryKey = PrimaryKey(baseAsset, quoteAsset, side)
}