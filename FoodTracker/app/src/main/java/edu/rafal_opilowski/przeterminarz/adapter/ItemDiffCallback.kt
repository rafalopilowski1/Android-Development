package edu.rafal_opilowski.przeterminarz.adapter

import androidx.recyclerview.widget.DiffUtil
import edu.rafal_opilowski.przeterminarz.model.Item

class ItemDiffCallback(
    private val old: List<Item>, val new: List<Item>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] === new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]
}