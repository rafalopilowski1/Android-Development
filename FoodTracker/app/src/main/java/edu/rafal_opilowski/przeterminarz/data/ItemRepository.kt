package edu.rafal_opilowski.przeterminarz.data

import edu.rafal_opilowski.przeterminarz.model.FormType
import edu.rafal_opilowski.przeterminarz.model.Item
import edu.rafal_opilowski.przeterminarz.model.Localizable
import edu.rafal_opilowski.przeterminarz.model.Type
import java.time.Instant

interface ItemRepository {
    fun getItemList(): List<Item>
    fun getItemList(filter: Localizable): List<Item>
    fun add(dish: Item)
    fun set(item: Item, type: Type, name: String, count: Pair<String, Int>?, expiryDate: Instant)
    fun getItemById(id: Int): Item
    fun remove(it: Item)
}