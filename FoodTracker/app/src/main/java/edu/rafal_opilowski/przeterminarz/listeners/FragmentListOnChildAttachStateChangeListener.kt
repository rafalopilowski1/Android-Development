package edu.rafal_opilowski.przeterminarz.listeners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rafal_opilowski.przeterminarz.adapter.ItemListAdapter
import edu.rafal_opilowski.przeterminarz.databinding.FragmentListBinding

class FragmentListOnChildAttachStateChangeListener(
    private val binding: FragmentListBinding,
    private val itemListAdapter: ItemListAdapter
) : RecyclerView.OnChildAttachStateChangeListener {
    override fun onChildViewAttachedToWindow(view: View) {
        with(binding.summaryTextView) {
            text = itemListAdapter.itemCount.toString()
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        with(binding.summaryTextView) {
            text = itemListAdapter.itemCount.toString()
        }
    }
}