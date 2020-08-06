package com.enciyo.pinbook.ui.login

import androidx.lifecycle.MutableLiveData
import com.enciyo.pinbook.domain.model.User
import com.enciyo.pinbook.reducer.SideEffect

data class LoginSideEffect(
    val user: MutableLiveData<User> = MutableLiveData()
) : SideEffect()



