package ua.cryptogateway.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.cryptogateway.data.models.Order.Side.Buy
import ua.cryptogateway.data.models.Order.Side.Sell

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
}