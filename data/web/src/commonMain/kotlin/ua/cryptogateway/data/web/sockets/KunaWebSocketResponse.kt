package ua.cryptogateway.data.web.sockets

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.cryptogateway.data.models.Trade

@Serializable
sealed interface KunaWebSocketResponse {
    val event: String


    @Serializable
    @SerialName("#setAuthToken")
    data class SetAuthToken(
        override val event: String,
        val data: Data,
    ) : KunaWebSocketResponse {
        @Serializable
        data class Data(val token: String)
    }

    @Serializable
    @SerialName("#publish")
    data class PublishMessage(
        override val event: String,
        val data: Data,
    ) : KunaWebSocketResponse {
        @Serializable
        data class Data(
            val channel: String,
            val data: ChannelData
        )
    }
}


@Serializable
sealed interface ChannelData {

    /**
     * Individual Symbol Ticker Stream
     *
     * Stream Name: <symbol>@ticker
     * Update Speed: 1000ms
     *
     * Description: The <symbol>@ticker is a real-time data stream that provides updates on the latest
     *  trading price and other market data for a specific trading pair.
     *
     * The <symbol> refers to the trading pair, such as btc_usdt or eth_btc, and the @ticker keyword
     * indicates that the stream provides updates on the ticker for that specific trading pair.
     *
     * The data in the <symbol>@ticker stream includes the latest trading price, high and low prices,
     * trading volume, and the percentage change in price, etc. (see type Ticker above). As the trading
     * price changes, the <symbol>@ticker stream updates, providing up-to-date market information.
     *
     * The <symbol>@ticker stream is useful to stay informed about the latest price movements for a specific
     * trading pair and make informed trading decisions based on the latest market data.
     *
     * @property event The type of the event (e.g., ticker updates).
     * @property data The detailed market data associated with the ticker event.
     */
    @Serializable
    @SerialName("ticker")
    data class Ticker(
        val event: String,
        val data: Data,
    ) : ChannelData {

        @Serializable
        data class Data(
            @SerialName("p") val pair: String,                  // pair
            @SerialName("pc") val priceChange: Double,          // Price change
            @SerialName("pcp") val priceChangePercent: Double,  // Price change percent
            @SerialName("ftbp") val firstTradePrice: Double,    // First trade(F)-1 price (first trade before the 24hr rolling window)
            @SerialName("lp") val lastPrice: Double,            // Last price
            @SerialName("lq") val lastQuantity: Double,         // Last quantity
            @SerialName("bbp") val bestBidPrice: Double,        // Best bid price
            @SerialName("bbq") val bestBidQuantity: Double,     // Best bid quantity
            @SerialName("bap") val bestAskPrice: Double,        // Best ask price
            @SerialName("baq") val bestAskQuantity: Double,     // Best ask quantity
            @SerialName("o") val openPrice: Double,             // Open price
            @SerialName("h") val highPrice: Double,             // High price
            @SerialName("l") val lowPrice: Double,              // Low price
            @SerialName("ot") val statisticsOpenTime: Instant,  // Statistics open time
            @SerialName("ct") val statisticsCloseTime: Instant, // Statistics close time
            @SerialName("n") val trades: Int,                   // Total number of trades
            @SerialName("ttbav") val totalBaseAssetVolume: Double,  // Total traded base asset volume
            @SerialName("ttqav") val totalQuoteAssetVolume: Double, // Total traded quote asset volume
        )
    }

    /**
     * All Market Tickers Stream
     *
     * Stream Name: arrTicker
     * Update Speed: 1000ms
     *
     * Description: The arrTicker stream contains an array of Ticker objects, each representing a different trading pair on the exchange.
     *
     * @property event The type of WebSocket event that occurred (e.g., "arrTicker").
     * @property data A list of individual [Ticker.Data] objects, each representing mini ticker information
     * for a specific trading pair.
     */
    @Serializable
    @SerialName("arrTicker ")
    data class ArrTicker(
        val event: String,
        val data: List<Ticker.Data>,
    ) : ChannelData

    /**
     * Individual Symbol Mini Ticker Stream
     *
     * Stream Name: <symbol>@miniTicker
     * Update Speed: 1000ms
     *
     * Description: The <symbol>@miniTicker is a real-time data stream that provides updates on the latest trading
     *  price and other market data for a specific trading pair in a compact format.
     *
     *  The <symbol>@miniTicker stream is designed to be more lightweight than the <symbol>@ticker stream,
     *  making it ideal for applications that require a lower network bandwidth or data usage. You can use the
     *  <symbol>@miniTicker stream to quickly check the latest market data for a specific trading pair without
     *  consuming excessive network resources.
     *
     * @property event The type of the WebSocket event.
     * @property data The detailed information related to the mini ticker event, encapsulated within the
     * nested [Data] data class.
     */
    @Serializable
    @SerialName("miniTicker")
    data class MiniTicker(
        val event: String,
        val data: Data,
    ) : ChannelData {

        @Serializable
        data class Data(
            @SerialName("p") val pair: String,      // pair
            @SerialName("o") val openPrice: Double, // Open price
            @SerialName("h") val highPrice: Double, // High price
            @SerialName("l") val lowPrice: Double,  // Low price
            @SerialName("c") val lastPrice: Double, // Last price
            @SerialName("n") val trades: Int,       // Total number of trades
            @SerialName("ttbav") val totalBaseAssetVolume: Double,  // Total traded base asset volume
            @SerialName("ttqav") val totalQuoteAssetVolume: Double, // Total traded quote asset volume
        )
    }

    /**
     * All Market Mini Tickers Stream
     *
     * Stream Name: arrMiniTicker
     * Update Speed: 1000ms
     *
     * Description: The arrMiniTicker stream contains an array of MiniTicker objects,
     *  each representing a different trading pair on the exchange.
     *
     * @property event The type of the WebSocket event, indicating the type of data being received.
     * @property data A list of mini ticker data entries, each representing compact market details
     * for a specific trading pair.
     */
    @Serializable
    @SerialName("arrMiniTicker")
    data class ArrMiniTicker(
        val event: String,
        val data: List<MiniTicker.Data>,
    ) : ChannelData

    /**
     * General information: OHLCV is an acronym that stands for Open, High, Low, Close, and Volume and refers to a type
     * of chart used to represent the price movement of a financial asset.
     *
     * Each element of the acronym represents a different data point that is plotted on the chart:
     *
     * Open: The opening price of the asset during a specific time period (e.g., day, hour, minute).
     * High: The highest price that the asset reached during the time period.
     * Low: The lowest price that the asset reached during the time period.
     * Close: The closing price of the asset at the end of the time period.
     * Volume: The total trading volume of the asset during the time period.
     *
     * OHLCV
     *
     * Stream Name: <symbol>@ohlcv
     * Update Speed: 2000ms
     *
     * Description: The <symbol>@ohlcv stream provides a continuous feed of the most recent OHLCV data for the specified
     *  trading pair. This data can be used to track the price movement of the trading pair over time and identify trends
     *  or patterns in the data and build charts for visualisation.
     *
     * @property event Specifies the type of the event, such as subscription or update, for this OHLCV data.
     * @property data Contains detailed OHLCV data, encapsulated in the nested [Data] class.
     */
    @Serializable
    @SerialName("ohlcv")
    data class Ohlcv(
        val event: String,
        val data: Data
    ) : ChannelData {

        @Serializable
        data class Data(
            @SerialName("ot") val openTime: Long,       // open time
            @SerialName("ct") val closeTime: Long,      // close time
            @SerialName("t") val trades: Int,           // trades
            @SerialName("o") val openPrice: Double,     // open price
            @SerialName("h") val highPrice: Double,     // high price
            @SerialName("l") val lowPrice: Double,      // low price
            @SerialName("c") val closePrice: Double,    // close price
            @SerialName("v") val volume: Double         // volume
        )
    }

    /**
     * General information: Trade occurs when a buyer and a seller agree to exchange one financial asset for another.
     * The trade is executed at a specific price, which is agreed upon by the buyer and the seller.
     *
     * Individual Symbol Trade Stream
     *
     * Stream Name: <symbol>@trade
     * Update Speed: Real-time
     *
     * Description: The <symbol>@trade stream provides real-time updates for the latest trades executed for a specific trading pair.
     *  Each trade update includes information such as the trade ID, the price at which the trade was executed,
     *  the quantity of the asset traded, the timestamp of the trade, etc.
     *
     * @property event The name of the WebSocket event.
     * @property data The detailed data of the trade event.
     */
    @Serializable
    @SerialName("trade")
    data class Trade(
        val event: String,
        val data: Data
    ) : ChannelData {

        @Serializable
        data class Data(
            val id: String,
            val sn: String,
            val pair: String,
            val matchPrice: Double,
            val matchQuantity: Double,
            val quoteQuantity: Double,
            val type: Trade.Type,
            val createdAt: Instant,
        )
    }

    /**
     * Individual Symbol Aggregated Trade Stream
     *
     * Stream Name: <symbol>@aggTrade
     * Update Speed: Real-time
     *
     * Description: The aggregated trade stream differs from the regular trade stream in that it provides summarized information for multiple
     *  trades executed at the same price and time, instead of individual updates for each trade. The stream also includes a trade ID representing
     *  the first and last trade executed in the aggregation, which can be used to reference the aggregated trades.
     *
     * @property event The event type of the message, typically "aggTrade".
     * @property data The associated `Data` object containing detailed aggregated trade information.
     */
    @Serializable
    @SerialName("aggTrade")
    data class AggTrade(
        val event: String,
        val data: Data
    ) : ChannelData {

        @Serializable
        data class Data(
            val id: String,
            val pair: String,
            val matchPrice: Double,
            val matchQuantity: Double,
            val type: Trade.Type,
            val createdAt: Instant,
            val firstTradeId: String,
            val lastTradeId: String,
        )
    }
}