package com.enciyo.pinbook.data.cache

import com.enciyo.pinbook.data.PinBookCacheDataSource
import com.enciyo.pinbook.domain.model.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PinBookCacheDataSourceImp @Inject constructor(
    private val mmkv: MMKV
) : PinBookCacheDataSource {
  companion object{
    const val KEY_CURRENT_USER = "KeyCurrentUser"
  }

  override suspend fun currentUser() : User? = withContext(Dispatchers.IO){
    getCurrentUserAndMapping()
  }

  override suspend fun setCurrentUser(user: User) = withContext(Dispatchers.IO){
    setCurrentUserAndMapping(user)
  }

  private fun getCurrentUserAndMapping(): User? {
    val userModelJson:String? = mmkv.decodeString(KEY_CURRENT_USER,null)
    if (userModelJson.isNullOrBlank()) return null
    return Gson().fromJson(userModelJson,User::class.java)
  }
  private suspend fun setCurrentUserAndMapping(user: User) = withContext(Dispatchers.IO){
    val userModelJson = Gson().toJson(user)
    mmkv.encode(KEY_CURRENT_USER,userModelJson)
  }


}
