package com.enciyo.pinbook.utils.resourcewrapper

import androidx.annotation.StringRes


interface ResourceWrapper {
    fun getString(@StringRes idRes: Int): String
}