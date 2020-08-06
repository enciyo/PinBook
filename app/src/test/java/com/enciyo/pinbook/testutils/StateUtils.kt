package com.enciyo.pinbook.testutils

import com.enciyo.pinbook.reducer.ActionState
import com.enciyo.pinbook.reducer.SideEffect
import com.enciyo.pinbook.reducer.State
import com.enciyo.pinbook.reducer.ViewState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList


inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assertViewState(boolean: V? = null) {
  assert(boolean == this.view) {
    "\n actual: ${if (this.view == null) "Null" else this.view!!::class.java.name} \n" +
        "expected: ${if (boolean == null) "Null" else boolean::class.java.name}"
            .trimMargin()
  }
}

inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assertAction(boolean: A? = null) {
  assert(boolean == this.action?.peekContent()) {
    "\n actual: ${if (this.action?.peekContent() == null) "Null" else this.action?.peekContent()!!::class.java.name} \n" +
        "expected: ${if (boolean == null) "Null" else boolean::class.java.name}"
            .trimMargin()
  }
}

inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assertSideEffect(boolean: S? = null) {
  assert(boolean == this.sideEffect) {
    "\n actual: ${if (this.sideEffect == null) "Null" else sideEffect!!::class.java.name} \n" +
        "expected: ${if (boolean == null) "Null" else boolean::class.java.name}"
            .trimMargin()
  }
}

inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assert(boolean: V? = null) = this.assertViewState(boolean)
inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assert(boolean: A? = null) = this.assertAction(boolean)
inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> State<V, A, S>.assert(boolean: S? = null) = this.assertSideEffect(boolean)


suspend inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> StateFlow<State<V, A, S>>.lastState(take: Int = 1) = this.take(take).toList().last()

