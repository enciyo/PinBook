package com.enciyo.pinbook.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.enciyo.pinbook.data.db.entity.BookShowcaseEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookShowcaseDao {

  @Query("SELECT * FROM bookshowcaseentity")
  fun getAllShowcaseBooks() : Flow<List<BookShowcaseEntity>>

  @Query("SELECT * FROM bookshowcaseentity")
 suspend fun getBlockAllShowcaseBooks() : List<BookShowcaseEntity>

  @Insert
  suspend fun insert(showcase: List<BookShowcaseEntity>)

  @Query("DELETE FROM  bookshowcaseentity")
  suspend fun deleteAll()

}