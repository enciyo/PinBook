package com.enciyo.pinbook.domain.mapper

import com.enciyo.pinbook.data.remote.model.ResponseUser
import com.enciyo.pinbook.domain.model.User


fun ResponseUser.toUser() = User(
    id = id,
    displayName = mDisplayName,
    mail = mEmail,
    password = mPassword,
    username = mUsername
)
