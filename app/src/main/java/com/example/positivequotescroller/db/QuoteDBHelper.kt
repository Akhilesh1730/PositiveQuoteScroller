package com.example.positivequotescroller.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.positivequotescroller.model.QuoteListItem
import com.example.positivequotescroller.model.SavedItem

class QuoteDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val db: SQLiteDatabase = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME " +
                "($ID INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                "$QUOTES_COl TEXT, " +
                "$AUTHOR_COL TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE $TABLE_NAME")
        onCreate(db)
    }

    fun addSavedQuotes(item : QuoteListItem): Long {
        val value = ContentValues()
        value.put(QUOTES_COl, item.q)
        value.put(AUTHOR_COL, item.a)
        return db.insert(TABLE_NAME, null, value)
    }

    fun getSavedQuotes() : MutableList<SavedItem> {
        val cursor = db?.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val list = mutableListOf<SavedItem>()
        while (cursor?.moveToNext() == true) {
            list.add(SavedItem(cursor.getInt(0),cursor.getString(1), cursor.getString(2)))
        }
        return list
    }

    fun checkQuotesAdded(quotes : String) : Boolean {
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        while (cursor.moveToNext()) {
            if (quotes == cursor.getString(1)) return true
        }
        return false
    }
    fun deleteSavedQuotes(item: SavedItem): Int {
        return db.delete(TABLE_NAME, "id = ${item.id}", null)
    }

    companion object{
        private val DATABASE_NAME = "QuotesDB"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "quotes"
        val ID = "id"
        val QUOTES_COl = "quotes"
        val AUTHOR_COL = "author"
    }
}