package com.enciyo.pinbook.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BookCategoryEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "category_id") val categoryId:String,
    @ColumnInfo(name = "category_name") val categoryName:String
)