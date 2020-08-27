package com.enciyo.pinbook.data.db.dao

import androidx.room.*
import com.enciyo.pinbook.data.db.entity.BookFavoriteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookFavoriteEntityDao {


    @Query("SELECT * FROM BookFavoriteEntity")
    fun getAllBooks(): Flow<MutableList<BookFavoriteEntity>>


    @Query("SELECT * FROM BookFavoriteEntity WHERE book_id =:bookID")
    fun getBook(bookID: Int): BookFavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookEntity: List<BookFavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookEntity: BookFavoriteEntity): Long

    @Delete
    suspend fun delete(bookEntity: BookFavoriteEntity)

    @Query("DELETE FROM BookFavoriteEntity")
    suspend fun deleteAll()


}