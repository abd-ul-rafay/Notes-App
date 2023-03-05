package dev.abdulrafay.notes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query ("select * from notes")
    fun getNotes(): LiveData<List<Notes>>

    @Query ("delete from notes")
    fun deleteAllNotes()

}
