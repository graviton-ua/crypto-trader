package ua.crypto.ui.home

abstract class ComposeOrder {
    abstract val type: String       // Limit, Market, StopLossLimit, TakeProfitLimit
    abstract val orderSide: String  // Bid, Ask
    abstract val pair: String       // BTC_USDT
    open var quantity: Double? = null    // Precision depends on the asset as per Get all traded markets endpoint.
    abstract var price: Double
    open val quoteQuantity: Double? = null  // This field is only available for Market orders.
    open val stopPrice: Double? =
        null   // It is a trigger for the limit price: when the coin's market price reaches stopPrice, a limit order is automatically placed.

    abstract fun makePrice(): Double
    abstract fun makeQuantity(): Double?
}

interface HomeComposeOrder {
    val type: String
    val orderSide: String
    val pair: String

    fun makePrice(): Double {
        return 0.0
    }
}

interface Kira {
    val name: String
}

class Andrew(
    override val type: String,
    override val orderSide: String,
    override val pair: String,
) : HomeComposeOrder, Kira {

    override val name: String
        get() = "asdasd"

}



/**
 * Order type       Available parameters
 * Limit            type, orderSide, pair, quantity, price
 */
class ComposeLimit(override val orderSide: String, override val pair: String) : ComposeOrder() {
    override val type = "limit"
    override var price = 0.0
    override var quantity: Double? = 0.0

    override fun makePrice(): Double {
        price = 10.0
        return price
    }

    override fun makeQuantity(): Double? {
        quantity = 2.0
        return quantity
    }

    override fun equals(other: Any?): Boolean {
        return orderSide == (other as? ComposeLimit)?.orderSide
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}

/**
 * Order type       Available parameters
 * Market           type, orderSide, pair, quantity or quoteQuantity
 */

/**
 * StopLossLimit is needed to sell an asset below the market price and to buy above the market price.
 * Order type       Available parameters
 * StopLossLimit    type, orderSide, pair, quantity, price, stopPrice
 */

/**
 * TakeProfitLimit is needed to sell an asset above the market price and to buy below the market price
 * Order type       Available parameters
 * TakeProfitLimit  type, orderSide, pair, quantity, price, stopPrice
 */