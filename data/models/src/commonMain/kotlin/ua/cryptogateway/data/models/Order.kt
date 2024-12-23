package ua.cryptogateway.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.cryptogateway.data.models.Order.Side.Buy
import ua.cryptogateway.data.models.Order.Side.Sell
import ua.cryptogateway.data.models.Order.Type.*

interface Order {

    /**
     * Represents the side of an order in a trading system.
     *
     * This enum class specifies whether the order is for selling or buying.
     * The `Sell` side corresponds to "Ask", and the `Buy` side corresponds to "Bid".
     *
     * @property Sell Represents a sell order, commonly referred to as "Ask".
     * @property Buy Represents a buy order, commonly referred to as "Bid".
     */
    @Serializable
    enum class Side {
        @SerialName("Ask") Sell,
        @SerialName("Bid") Buy;
    }

    /**
     * Represents the type of order in a trading system.
     *
     * This enum class is used to specify the various types of orders
     * that can be placed in a trading environment.
     *
     * @property Limit A limit order, which specifies the maximum or minimum price at which
     *                 to buy or sell the asset.
     * @property Market A market order, which executes immediately at the best available price.
     * @property StopLossLimit A stop-limit order, which combines features of stop-loss
     *                          and limit orders to limit losses or lock in a profit.
     * @property TakeProfitLimit A take-profit-limit order, which helps secure profits
     *                            by specifying the price levels for profit-taking.
     */
    @Serializable
    enum class Type {
        @SerialName("Limit") Limit,
        @SerialName("Market") Market,
        @SerialName("StopLossLimit") StopLossLimit,
        @SerialName("TakeProfitLimit") TakeProfitLimit;
    }
}