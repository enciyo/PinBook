package com.enciyo.pinbook.reducer

import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * @author mustafa kilic
 */
@ExperimentalCoroutinesApi
abstract class BaseViewModel<V : ViewState, A : ActionState, S : SideEffect, I : UserInteractions>(
    private val reducer: Reducer<V, A, S>
) : HasUserIntercession<I>, HasReducerViewModel<V, A, S>(reducer)
