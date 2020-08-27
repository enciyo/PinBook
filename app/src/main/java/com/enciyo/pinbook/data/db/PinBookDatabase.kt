package com.enciyo.pinbook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enciyo.pinbook.data.db.dao.BookCategoryDao
import com.enciyo.pinbook.data.db.dao.BookDao
import com.enciyo.pinbook.data.db.dao.BookFavoriteEntityDao
import com.enciyo.pinbook.data.db.dao.BookShowcaseDao
import com.enciyo.pinbook.data.db.entity.BookCategoryEntity
import com.enciyo.pinbook.data.db.entity.BookEntity
import com.enciyo.pinbook.data.db.entity.BookFavoriteEntity
import com.enciyo.pinbook.data.db.entity.BookShowcaseEntity


@Database(
  entities = [
    BookCategoryEntity::class,
    BookEntity::class,
    BookShowcaseEntity::class,
    BookFavoriteEntity::class
  ], version = 11
)
abstract class PinBookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookCategoryDao(): BookCategoryDao
    abstract fun bookShowcaseDao(): BookShowcaseDao
    abstract fun bookFavoriteDao(): BookFavoriteEntityDao
}