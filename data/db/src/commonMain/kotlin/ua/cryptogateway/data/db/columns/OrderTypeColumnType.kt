package ua.cryptogateway.data.db.columns

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.EnumerationColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect
import ua.cryptogateway.data.db.models.OrderType
import kotlin.reflect.KClass

internal class OrderTypeColumnType : ColumnType<OrderType>() {
    private val klass: KClass<OrderType> = OrderType::class

    override fun sqlType(): String = currentDialect.dataTypeProvider.varcharType(8)
    //private val enumConstants by lazy { klass.java.enumConstants!! }

    @Suppress("UNCHECKED_CAST")
    override fun valueFromDB(value: Any): OrderType = when (value) {
        is String -> OrderType.valueOf(value)
        is Enum<*> -> value as OrderType
        else -> error("$value of ${value::class.qualifiedName} is not valid for enum ${klass.simpleName}")
    }

    override fun notNullValueToDB(value: OrderType): String = value.name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as EnumerationColumnType<*>

        return klass == other.klass
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + klass.hashCode()
        return result
    }
}