package edu.rafal_opilowski.digitaljournal.adapter

import androidx.recyclerview.widget.DiffUtil
import edu.rafal_opilowski.digitaljournal.model.Note

class NoteDiffCallback(
    val old: List<Note>,
    val new: List<Note>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] === new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]
}