package edu.rafal_opilowski.przeterminarz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rafal_opilowski.przeterminarz.R
import edu.rafal_opilowski.przeterminarz.adapter.ItemListAdapter
import edu.rafal_opilowski.przeterminarz.data.ItemRepository
import edu.rafal_opilowski.przeterminarz.data.RepositoryLocator
import edu.rafal_opilowski.przeterminarz.databinding.FragmentListBinding
import edu.rafal_opilowski.przeterminarz.listeners.FragmentListOnChildAttachStateChangeListener
import edu.rafal_opilowski.przeterminarz.model.FormType

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var itemRepository: ItemRepository
    private lateinit var itemListAdapter: ItemListAdapter
    private var chipClicked: MutableMap<String, Boolean> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemRepository = RepositoryLocator.itemRepository
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        itemListAdapter = ItemListAdapter {
            itemRepository.remove(it)
            itemListAdapter.itemList = itemRepository.getItemList()
        }
        itemListAdapter.itemList = itemRepository.getItemList()
        with(binding.itemList) {
            addOnChildAttachStateChangeListener(
                FragmentListOnChildAttachStateChangeListener(
                    binding,
                    itemListAdapter
                )
            )
        }
        binding.itemList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemListAdapter
        }
        with(binding.add) {
            setOnClickListener {
                findNavController().navigate(
                    R.id.action_listFragment_to_formFragment, bundleOf("type" to FormType.New)
                )
            }
        }
        with(binding.filter) {
            setOnClickListener {
                FilterBottomSheetFragment(itemRepository, itemListAdapter, chipClicked).show(
                    parentFragmentManager,
                    FilterBottomSheetFragment.TAG
                )
            }
        }
    }
    private fun onDestChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == R.id.main) {
            itemListAdapter.itemList = itemRepository.getItemList()
        }
    }
    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(::onDestChanged)
    }

    override fun onStop() {
        findNavController().addOnDestinationChangedListener(::onDestChanged)
        super.onStop()
    }
}