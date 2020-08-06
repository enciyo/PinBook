package com.enciyo.pinbook.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "book_id") val bookId:String,
    @ColumnInfo(name = "book_name") val bookName: String,
    @Embedded var bookCategory:BookCategoryEntity,
    @ColumnInfo(name = "book_author") val bookAuthor:String,
    @ColumnInfo(name = "book_page_of_count") val bookPageOfCount:String,
    @ColumnInfo(name = "book_rating") val bookRating:String,
    @ColumnInfo(name = "book_review_count") val bookReviewsCount:String,
    @ColumnInfo(name = "book_image") val bookImage:String
)
