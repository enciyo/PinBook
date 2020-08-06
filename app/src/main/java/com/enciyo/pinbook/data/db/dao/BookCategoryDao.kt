package com.enciyo.pinbook.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.enciyo.pinbook.data.db.entity.BookCategoryEntity


@Dao
interface BookCategoryDao{

  @Query("SELECT * FROM bookcategoryentity")
  fun getCategories() : MutableList<BookCategoryEntity>

  @Insert
  fun insertAll(vararg bookCategoryEntity: BookCategoryEntity)

  @Delete
  fun delete(bookCategoryEntity: BookCategoryEntity)


}