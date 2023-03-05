package dev.abdulrafay.notes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyRecyclerViewAdapter(private val context: Context, private var arrayListNotes: ArrayList<Notes>): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView
        var constraintLayout: ConstraintLayout
        var title: TextView
        var body: TextView
        var date: TextView

        init {
            cardView = itemView.findViewById(R.id.cardView)
            constraintLayout = itemView.findViewById(R.id.constraintLayout)
            title = itemView.findViewById(R.id.textViewTitleRV)
            body = itemView.findViewById(R.id.textViewBodyRV)
            date = itemView.findViewById(R.id.textViewDateRV)
        }
    }

    fun setFilteredList(arrayListNotes: ArrayList<Notes>) {
        this.arrayListNotes = arrayListNotes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (arrayListNotes[position].title.isEmpty()) {
            holder.title.visibility = View.GONE
            holder.body.textSize = 18F
        } else {
            holder.title.visibility = View.VISIBLE
            holder.body.textSize = 12F
            holder.title.text = arrayListNotes[position].title
        }

        if (arrayListNotes[position].body.isEmpty()) {
            holder.body.visibility = View.GONE
        } else {
            holder.body.visibility = View.VISIBLE
            holder.body.text = arrayListNotes[position].body
        }

        holder.date.text = arrayListNotes[position].date

        holder.cardView.setOnClickListener {
            val intent = Intent(context, AddNotesActivity::class.java)
            intent.putExtra("notesId", arrayListNotes[position].id)
            intent.putExtra("notesTitle", arrayListNotes[position].title)
            intent.putExtra("notesBody", arrayListNotes[position].body)
            intent.putExtra("notesDate", arrayListNotes[position].date)
            context.startActivity(intent)
        }


        holder.cardView.setOnLongClickListener {

            holder.constraintLayout.setBackgroundColor(Color.parseColor("#FF9494")) // error red color
            showDeleteDialog(holder, Notes(arrayListNotes[position].id, arrayListNotes[position].title, arrayListNotes[position].body, arrayListNotes[position].date))

            return@setOnLongClickListener true
        }

    }

    private fun showDeleteDialog(holder: ViewHolder, notes: Notes) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this note?")
            .setIcon(R.drawable.delete)

            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                GlobalScope.launch {
                    val database = NotesDBHelper.getInstance(context).noteDao().deleteNote(notes)
                }

                colorChanger(holder)
            })

            .setOnCancelListener(DialogInterface.OnCancelListener {
                colorChanger(holder)
            })

            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                colorChanger(holder)
            })
            .show()
    }

    private fun colorChanger(holder: ViewHolder) {
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                holder.constraintLayout.setBackgroundColor(Color.parseColor("#191919"))
            }
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                holder.constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayListNotes.size
    }
}
