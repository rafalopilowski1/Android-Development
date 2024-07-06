package edu.rafal_opilowski.przeterminarz.model

import java.time.Instant


data class Item(
    var type: Type, var name: String, var count: Pair<String, Int>?, var expiryDate: Instant
) : Comparable<Item> {
    val expired: Expiration
        get() = if (expiryDate.isBefore(Instant.now())) Expiration.EXPIRED
        else Expiration.USABLE

    override fun compareTo(other: Item): Int = expiryDate.compareTo(other.expiryDate)

}
