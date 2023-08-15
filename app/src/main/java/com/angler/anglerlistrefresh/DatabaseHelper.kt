package com.angler.anglerlistrefresh

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "image_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "images"
        private const val COLUMN_ID = "id"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_IMAGE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertImage(imageResourceId: Int): Long {
        val values = ContentValues()
        values.put(COLUMN_IMAGE, imageResourceId)

        return writableDatabase.insert(TABLE_NAME, null, values)
    }
    fun clearImages() {
        writableDatabase.delete(TABLE_NAME, null, null)
    }

    fun getImagesInBatch(startIndex: Int, batchSize: Int): List<Int> {
        val imagesList = mutableListOf<Int>()
        val query = "SELECT $COLUMN_IMAGE FROM $TABLE_NAME LIMIT $batchSize OFFSET $startIndex"
        val cursor = readableDatabase.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val imagePath = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE))
            imagesList.add(imagePath)
        }

        cursor.close()
        return imagesList
    }


    fun getAllImages(): List<String> {
        val imagesList = mutableListOf<String>()
        val cursor = readableDatabase.rawQuery("SELECT $COLUMN_IMAGE FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            val imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
            imagesList.add(imagePath)
        }

        cursor.close()
        return imagesList
    }
}
