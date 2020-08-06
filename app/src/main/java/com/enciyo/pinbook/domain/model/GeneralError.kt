package com.enciyo.pinbook.domain.model


data class GeneralError(
    val error: String,
    override val message: String,
    val status: Int
) : Exception()