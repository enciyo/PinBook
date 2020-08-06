package com.enciyo.pinbook.data

import com.enciyo.pinbook.domain.model.User


interface PinBookCacheDataSource{

  suspend fun currentUser() : User?
  suspend fun setCurrentUser(user: User) : Boolean


}