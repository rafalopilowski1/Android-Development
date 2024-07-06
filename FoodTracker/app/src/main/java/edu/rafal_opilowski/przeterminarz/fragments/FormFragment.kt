package edu.rafal_opilowski.przeterminarz.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rafal_opilowski.przeterminarz.R
import edu.rafal_opilowski.przeterminarz.data.ItemRepository
import edu.rafal_opilowski.przeterminarz.data.RepositoryLocator
import edu.rafal_opilowski.przeterminarz.databinding.FragmentFormBinding
import edu.rafal_opilowski.przeterminarz.model.FormType
import edu.rafal_opilowski.przeterminarz.model.Item
import edu.rafal_opilowski.przeterminarz.model.Type
import edu.rafal_opilowski.przeterminarz.utilities.showSnackbar
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

private const val TYPE_KEY = "type"

class FormFragment : Fragment() {
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private lateinit var repository: ItemRepository

    private var chosenType: Type = Type.entries.random()
    private val calendar = Calendar.getInstance()
    private val today = calendar.toInstant()
    private var edited: Item? = null
    private var minChosenDate: Instant = today.plus(1, ChronoUnit.DAYS)
    private var chosenDate: Instant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = RepositoryLocator.itemRepository
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentFormBinding.inflate(layoutInflater, container, false)
            .also { binding = it }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.autoComplete) {
            setAdapter(
                ArrayAdapter(context,
                    R.layout.simple_spinner_item,
                    Type.entries.map { it.toLocalizedString(context) })
            )
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                chosenType = Type.entries[position]
            }
        }

        (type as? FormType.Edit)?.let { formType ->
            edited = formType.item
            with(formType.item) {
                chosenType = type
                chosenDate = expiryDate
                with(binding.nameField) { setText(name) }
                count?.let {
                    with(binding.countNumberPicker) { text.append(it.second.toString()) }
                    with(binding.amountCategoryTextView) { text.append(it.first) }
                }
            }
        }
        (binding.categoryTextInput.editText as? AutoCompleteTextView)?.setText(
            context?.let { chosenType.toLocalizedString(it) }, false
        )
        changeDisplayDate()
        with(binding.changeDateButton) {
            setOnClickListener { popUpDatePickerDialog(context) }
        }
        with(binding.button) {
            text = when (type) {
                is FormType.Edit -> getString(R.string.save)
                FormType.New -> getString(R.string.add)
            }
            setOnClickListener {
                if (saveItem(type)) findNavController().popBackStack()
            }
        }
    }

    private fun popUpDatePickerDialog(context: Context) {
        val dialog = DatePickerDialog(context)
        with(dialog) {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                chosenDate = calendar.toInstant()
                changeDisplayDate()
            }
            setOnShowListener { changeDateButtonEnabled(false) }
            setOnDismissListener { changeDateButtonEnabled(true) }
            setOnCancelListener { changeDateButtonEnabled(true) }
            datePicker.minDate = minChosenDate.toEpochMilli()
            show()
        }
    }

    private fun changeDateButtonEnabled(enabled: Boolean) {
        with(binding.changeDateButton) {
            isEnabled = enabled
        }
    }

    private fun changeDisplayDate() {
        val date: Instant? = chosenDate
        with(binding.dateDisplayTextView) {
            text = date?.let {
                DateTimeFormatter.ISO_LOCAL_DATE.format(date.atZone(ZoneId.systemDefault()))
                    .toString()
            } ?: "XXXX-XX-XX"
        }
    }

    private fun saveItem(type: FormType): Boolean {
        val name = binding.nameField.text.toString()
        if (name.isEmpty()) {
            showSnackbar(requireView(), getString(R.string.type_in_name_of_an_product))
            return false
        }
        val countText = binding.countNumberPicker.text
        val countCategoryText = binding.amountCategoryTextView.text
        if (countCategoryText.isEmpty() && countText.isEmpty().not()) {
            showSnackbar(requireView(), getString(R.string.choose_unit_of_an_product))
            return false
        }
        val count: Pair<String, Int>? = when {
            countText.isEmpty() or countCategoryText.isEmpty() -> null
            else -> countCategoryText.toString() to countText.toString().toInt()
        }
        val expiryDate = chosenDate
        if (expiryDate == null) {
            showSnackbar(requireView(), getString(R.string.choose_expiry_date_of_an_product))
            return false
        }
        when (type) {
            is FormType.New -> repository.add(Item(chosenType, name, count, expiryDate))
            is FormType.Edit -> {
                with(type) {
                    repository.set(item, chosenType, name, count, expiryDate)
                }
            }
        }
        return true
    }
}