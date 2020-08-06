package com.enciyo.pinbook.ui.register

import com.enciyo.pinbook.reducer.ViewState




data class RegisterViewState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var isFailure : Boolean = false
) : ViewState()