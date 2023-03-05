package dev.abdulrafay.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "notes")
class Notes: Serializable {

    @PrimaryKey (autoGenerate = true)
    var id: Int = 0
    @ColumnInfo (name = "title")
    val title: String
    @ColumnInfo (name = "body")
    val body: String
    @ColumnInfo (name = "data")
    val date: String

    constructor(id: Int, title: String, body: String, date: String) {
        this.id = id
        this.title = title
        this.body = body
        this.date = date
    }

    @Ignore
    constructor(title: String, body: String, date: String) {
        this.title = title
        this.body = body
        this.date = date
    }

}
