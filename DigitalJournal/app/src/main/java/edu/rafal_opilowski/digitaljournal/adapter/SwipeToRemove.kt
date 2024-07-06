package edu.rafal_opilowski.digitaljournal.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToRemove(private val onSwipe: (Int) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe((viewHolder as NoteItem).id)
    }
}