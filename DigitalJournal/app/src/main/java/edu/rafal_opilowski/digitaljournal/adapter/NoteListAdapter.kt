package edu.rafal_opilowski.digitaljournal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rafal_opilowski.digitaljournal.databinding.ItemFoodBinding
import edu.rafal_opilowski.digitaljournal.model.Note

class NoteItem(private val itemViewBinding: ItemFoodBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {
    var id: Int = 0
        private set

    fun onBind(note: Note, onItemClick: (Int) -> Unit) = with(itemViewBinding) {
        id = note.id
        title.text = note.title
        description.text = note.description
        if (note.city != null) city.text = note.city.locality
        else {
            city.visibility = View.GONE
            locationIcon.visibility = View.GONE
        }
        photoIcon.visibility = if (note.image_id != null) View.VISIBLE else View.GONE
        recordingIcon.visibility = if (note.recording_id != null) View.VISIBLE else View.GONE
        root.setOnClickListener { onItemClick(note.id) }
    }
}

class NoteListAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<NoteItem>() {
    var noteList: List<Note> = emptyList()
        set(value) {
            val diffs = DiffUtil.calculateDiff(NoteDiffCallback(field, value))
            field = value
//            notifyDataSetChanged()
            diffs.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(layoutInflater, parent, false)
        // .inflate(R.layout.item_food,parent,false)
        return NoteItem(binding)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteItem, position: Int) {
        holder.onBind(noteList[position]) { onItemClick(it) }
    }

}