package com.enciyo.pinbook.utils.resourcewrapper

import android.content.Context
import androidx.annotation.StringRes


class ResourceWrapperImp(
    private val mApplicationContext : Context) : ResourceWrapper {

    override fun getString(@StringRes idRes: Int): String {
        return mApplicationContext.getString(idRes)
    }

}