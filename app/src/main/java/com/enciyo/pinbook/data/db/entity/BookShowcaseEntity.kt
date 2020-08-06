package com.enciyo.pinbook.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BookShowcaseEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "book_showcase_id") val bookId:String,
    @ColumnInfo(name = "book_image_url") val imageUrl:String,
    @ColumnInfo(name = "book_name")val bookName:String
)