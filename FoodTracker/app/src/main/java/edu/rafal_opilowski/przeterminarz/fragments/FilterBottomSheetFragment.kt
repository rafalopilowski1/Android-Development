package edu.rafal_opilowski.przeterminarz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import edu.rafal_opilowski.przeterminarz.adapter.ItemListAdapter
import edu.rafal_opilowski.przeterminarz.data.ItemRepository
import edu.rafal_opilowski.przeterminarz.databinding.FilterBottomSheetLayoutBinding
import edu.rafal_opilowski.przeterminarz.model.Expiration
import edu.rafal_opilowski.przeterminarz.model.Localizable
import edu.rafal_opilowski.przeterminarz.model.Type

class FilterBottomSheetFragment(
    private val itemRepository: ItemRepository,
    private val itemListAdapter: ItemListAdapter,
    private val chipClicked: MutableMap<String, Boolean>
) : BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomSheetLayoutBinding
    private var typeChips: Map<Type, Chip> = mapOf()
    private var expirationChips: Map<Expiration, Chip> = mapOf()

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FilterBottomSheetLayoutBinding.inflate(layoutInflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expirationChips = createChips(Expiration.entries)
        typeChips = createChips(Type.entries)
        associateChipWithAnother(typeChips, expirationChips)
        restoreChipHistory(typeChips)
        restoreChipHistory(expirationChips)
        addChips(binding.expirationChipGroup, expirationChips)
        addChips(binding.categoryChipGroup, typeChips)
    }

    private fun <T> restoreChipHistory(typeChips: Map<T, Chip>) {
        typeChips.forEach { (it, chip) ->
            chip.isChecked = chipClicked.getOrPut(it.toString()) { false }
        }
    }

    private fun <T1 : Localizable, T2 : Localizable> associateChipWithAnother(
        chipMap1: Map<T1, Chip>, chipMap2: Map<T2, Chip>
    ) {
        chipMap1.forEach { (filter, chip) ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                chipOnCheckedChange(filter, chipMap2, isChecked)
            }
        }
        chipMap2.forEach { (filter, chip) ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                chipOnCheckedChange(filter, chipMap1, isChecked)
            }
        }
    }

    private fun <T1> addChips(chipGroup: ChipGroup, chipMap: Map<T1, Chip>) {
        with(chipGroup) {
            isSelectionRequired = false
            isSingleSelection = true
            chipMap.forEach { (_, chip) -> addView(chip) }
        }
    }

    private fun <T : Localizable> createChips(
        iterable: Iterable<T>,
    ): Map<T, Chip> = iterable.associateWith {
        val chip = Chip(context)
        chip.text = context?.let { ctx -> it.toLocalizedString(ctx) }
        chip.id = ViewGroup.generateViewId()
        chip.isCheckable = true
        chip
    }

    private fun <T> chipOnCheckedChange(
        filter: Localizable, chips: Map<T, Chip>, isChecked: Boolean
    ) {
        chipClicked[filter.toString()] = isChecked
        itemListAdapter.itemList = if (isChecked) itemRepository.getItemList(filter)
        else itemRepository.getItemList()
        chips.forEach { (_, chip) ->
            chip.isEnabled = !isChecked
        }
    }
}



