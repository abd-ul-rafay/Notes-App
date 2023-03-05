package dev.abdulrafay.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class], exportSchema = false, version = 1)
abstract class NotesDBHelper: RoomDatabase() {

    companion object {
        private var instance: NotesDBHelper? = null

        fun getInstance(context: Context): NotesDBHelper {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        NotesDBHelper::class.java,
                        "NotesDatabase"
                    ).build()
                }
            }

            return instance!!
        }
    }

    abstract fun noteDao(): NoteDao

}
