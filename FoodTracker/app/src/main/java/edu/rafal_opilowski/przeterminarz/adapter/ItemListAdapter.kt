package edu.rafal_opilowski.przeterminarz.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rafal_opilowski.przeterminarz.R
import edu.rafal_opilowski.przeterminarz.databinding.ListitemItemBinding
import edu.rafal_opilowski.przeterminarz.model.Expiration
import edu.rafal_opilowski.przeterminarz.model.FormType
import edu.rafal_opilowski.przeterminarz.model.Item
import edu.rafal_opilowski.przeterminarz.utilities.showSnackbar
import edu.rafal_opilowski.przeterminarz.viewmodel.ItemViewModel
import java.time.Instant

class ItemListAdapter(
    private val removeOnLongClick: (Item) -> Unit,
) : RecyclerView.Adapter<ItemViewModel>() {
    var itemList: List<Item> = emptyList()
        set(value) {
            val diffs = DiffUtil.calculateDiff(ItemDiffCallback(field, value))
            field = value
            diffs.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewModel {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListitemItemBinding.inflate(layoutInflater, parent, false)
        return ItemViewModel(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewModel, position: Int) {
        holder.onBind(itemList[position], { view, item ->
            with(view) {
                if (Instant.now().isAfter(item.expiryDate)) showSnackbar(
                    this, context.getString(R.string.this_item_has_expired_and_can_t_be_edited)
                )
                else findNavController().navigate(
                    R.id.action_listFragment_to_formFragment,
                    bundleOf("type" to FormType.Edit(item))
                )
            }
        }, { view, item ->
            if (item.expired != Expiration.EXPIRED)
                with(view) {
                    AlertDialog.Builder(context).setCancelable(true)
                        .setMessage(context.getString(R.string.do_you_for_sure_want_to_delete_this_element))
                        .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                            run {
                                showSnackbar(
                                    this, context.getString(R.string.you_ve_deleted, item.name)
                                )
                                removeOnLongClick(itemList[position])
                            }
                        }
                        .setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                        .show().isShowing
                } else false
        })
    }
}