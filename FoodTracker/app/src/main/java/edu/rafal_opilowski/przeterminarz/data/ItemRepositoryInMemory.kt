package edu.rafal_opilowski.przeterminarz.data

import android.icu.util.Calendar
import edu.rafal_opilowski.przeterminarz.model.Expiration
import edu.rafal_opilowski.przeterminarz.model.FormType
import edu.rafal_opilowski.przeterminarz.model.Item
import edu.rafal_opilowski.przeterminarz.model.Localizable
import edu.rafal_opilowski.przeterminarz.model.Type
import java.time.Instant
import java.time.temporal.ChronoUnit

val calendar: Calendar = Calendar.getInstance()

object ItemRepositoryInMemory : ItemRepository {
    private val ItemList = mutableListOf(
        Item(
            Type.FOOD,
            "Serek Danio",
            Pair("sztuka", 1),
            Instant.ofEpochMilli(calendar.time.time).plus(3, ChronoUnit.DAYS)
        ), Item(
            Type.MEDICINE,
            "Ibuprofem",
            Pair("tabletki", 3),
            Instant.ofEpochMilli(calendar.time.time).plus(2, ChronoUnit.DAYS)
        ), Item(
            Type.COSMETICS,
            "Aloe Vera",
            null,
            Instant.ofEpochMilli(calendar.time.time).minus(1, ChronoUnit.DAYS)
        )
    ).sortedBy { it.expiryDate }.toMutableList()

    override fun getItemList(): List<Item> = ItemList.toList()
    override fun getItemList(filter: Localizable): List<Item> {
        (filter as? Expiration)?.let { expiration ->
            return getItemList().filter { it.expired == expiration }.toList()
        }
        (filter as? Type)?.let { type ->
            return getItemList().filter { it.type == type }.toList()
        }
        return getItemList()
    }

    override fun add(dish: Item) {
        ItemList.add(dish)
        ItemList.sortBy { it.expiryDate }
    }

    override fun set(
        item: Item,
        type: Type,
        name: String,
        count: Pair<String, Int>?,
        expiryDate: Instant
    ) {
        with(item) {
            this.type = type
            this.name = name
            this.count = count
            this.expiryDate = expiryDate
        }
        ItemList.sortBy { it.expiryDate }
    }

    override fun getItemById(id: Int): Item = getItemList()[id]

    override fun remove(it: Item) {
        ItemList.remove(it)
        ItemList.sortBy { it.expiryDate }
    }
}