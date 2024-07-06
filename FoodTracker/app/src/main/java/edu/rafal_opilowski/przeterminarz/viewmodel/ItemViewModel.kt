package edu.rafal_opilowski.przeterminarz.viewmodel

import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import edu.rafal_opilowski.przeterminarz.R
import edu.rafal_opilowski.przeterminarz.databinding.ListitemItemBinding
import edu.rafal_opilowski.przeterminarz.model.Expiration
import edu.rafal_opilowski.przeterminarz.model.Item
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ItemViewModel(
    private val itemViewBinding: ListitemItemBinding
) : RecyclerView.ViewHolder(itemViewBinding.root) {
    fun onBind(
        item: Item, onClick: (View, Item) -> Unit, onLongClick: (View, Item) -> Boolean
    ) {
        with(itemViewBinding) {
            productName.text = item.name
            category.text = item.type.toLocalizedString(root.context)
            item.count?.let {
                amount.visibility = VISIBLE
                amountLabel.visibility = VISIBLE
                with(root.context) {
                    amount.text = getString(R.string.count_with_unit, it.second, it.first)
                }
            }
            if (item.count == null) {
                amountLabel.visibility = GONE
                amount.visibility = GONE
            }
            if (item.expired == Expiration.EXPIRED) {
                expiryDate.setTextColor(Color.RED)
            }
            expiryDate.text = DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault())
                .format(item.expiryDate)
            with(root) {
                setOnClickListener { onClick(this, item) }
                setOnLongClickListener { onLongClick(this, item) }
            }
        }
    }
}
