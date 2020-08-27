package com.enciyo.pinbook.data.db.dao

import androidx.room.*
import com.enciyo.pinbook.data.db.entity.BookCategoryEntity


@Dao
interface BookCategoryDao {

    @Query("SELECT * FROM bookcategoryentity")
    fun getCategories(): MutableList<BookCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg bookCategoryEntity: BookCategoryEntity)

    @Delete
    fun delete(bookCategoryEntity: BookCategoryEntity)


}