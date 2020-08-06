package com.enciyo.pinbook.domain.mapper

import com.enciyo.pinbook.data.remote.model.ResponseError
import com.enciyo.pinbook.domain.model.GeneralError


fun ResponseError.toServerError() = GeneralError(
    error = mError.toString(),
    message = mMessage.toString(),
    status = mStatus?:0
)