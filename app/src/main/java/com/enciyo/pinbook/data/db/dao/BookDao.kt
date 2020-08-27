package com.enciyo.pinbook.data.db.dao

import androidx.room.*
import com.enciyo.pinbook.data.db.entity.BookEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookDao {

    @Query("SELECT * FROM bookentity")
    fun getAllBooks(): Flow<MutableList<BookEntity>>

    @Query("SELECT * FROM bookentity")
    suspend fun getBlockAllBooks(): MutableList<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookEntity: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookEntity: BookEntity): Long

    @Delete
    suspend fun delete(bookEntity: List<BookEntity>)

    @Query("DELETE FROM bookentity")
    suspend fun deleteAll()


}