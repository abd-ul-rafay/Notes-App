package dev.abdulrafay.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AddNotesActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var bodyEditText: EditText
    private lateinit var textViewDate: TextView
    private lateinit var title: String
    private lateinit var body: String
    private var id by Delegates.notNull<Int>()
    private lateinit var calendar: Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat
    private var date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        titleEditText = findViewById(R.id.addTitle)
        bodyEditText = findViewById(R.id.addBody)
        textViewDate = findViewById(R.id.textViewDate)

        id = intent.getIntExtra("notesId", 0)
        titleEditText.setText(intent.getStringExtra("notesTitle"))
        bodyEditText.setText(intent.getStringExtra("notesBody"))
        date = intent.getStringExtra("notesDate").toString()
        if (date != "null")
            textViewDate.text = date

        if (id == 0) {
            textViewDate.visibility = View.GONE
        } else {
            textViewDate.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.tickItem,
            android.R.id.home -> {

                if (id == 0) { // means id is new, now add
                    addInDatabase()
                } else {
                    updateInDatabase()
                }

                finish() // everything is done, now return back
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addInDatabase() {
        title = titleEditText.text.toString()
        body = bodyEditText.text.toString()

        getDateAndTime()

        if (title.trim().isEmpty() && body.trim().isEmpty()) {
            return
        }

        val database = NotesDBHelper.getInstance(this)
        GlobalScope.launch {
            database.noteDao().insertNote(Notes(title, body, date))
        }

        Toast.makeText(this, "Notes added", Toast.LENGTH_SHORT).show()
        Log.d("myDate", date)

    }

    private fun updateInDatabase() {
        title = titleEditText.text.toString()
        body = bodyEditText.text.toString()

        val database = NotesDBHelper.getInstance(this)

        if (title.isEmpty() && body.isEmpty()) { // while updating if user remove everything, just delete that note
            GlobalScope.launch {
                database.noteDao().deleteNote(Notes(id, title, body, date))
            }
            return
        }

        GlobalScope.launch {
            database.noteDao().updateNote(Notes(id, title, body, date))
        }

    }

    private fun getDateAndTime() {
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("HH:mm, dd-MM-yyyy")
        date = simpleDateFormat.format(calendar.time)
        textViewDate.text = date
    }

    override fun onBackPressed() {
        if (id == 0) { // means id is new, now add
            addInDatabase()
        } else {
            updateInDatabase()
        }

        super.onBackPressed()
    }
}
