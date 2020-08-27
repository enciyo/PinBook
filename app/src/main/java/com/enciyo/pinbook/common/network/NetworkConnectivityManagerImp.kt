package com.enciyo.pinbook.common.network

import android.content.Context
import com.enciyo.pinbook.utils.isNetworkAvailable


class NetworkConnectivityManagerImp(val context: Context) : NetworkConnectivityManager {
    override val isNetworkAvailable: Boolean
        get() = context.isNetworkAvailable()
}

