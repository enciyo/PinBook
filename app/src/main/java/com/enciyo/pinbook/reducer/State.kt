package com.enciyo.pinbook.reducer


import com.enciyo.pinbook.utils.livedata.Event


/**
 * @author mustafa kilic
 */
data class State<V : ViewState, A : ActionState, S : SideEffect>(val view: V? = null, val action: Event<A>? = null, val sideEffect: S? = null) {
  companion object {
    inline fun <reified V : ViewState, A : ActionState, reified S : SideEffect> create() = State<V, A, S>(V::class.java.newInstance(), null, S::class.java.newInstance())
  }
}



object Init: ViewState()
