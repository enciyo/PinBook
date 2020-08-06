package com.enciyo.pinbook.reducer

import android.util.Log
import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@ExperimentalCoroutinesApi
class ReducerImp<V : ViewState, A : ActionState, S : SideEffect>(private val tag: String? = null, private val a: State<V, A, S>) : Reducer<V, A, S> {

  companion object {
    inline fun <reified V : ViewState, reified A : ActionState, reified S : SideEffect> create(tag: String? = null): Reducer<V, A, S> {
      return ReducerImp<V, A, S>(tag, State.create<V,A,S>())
    }
  }

  private val _state: MutableStateFlow<State<V, A, S>> = MutableStateFlow(a)
  override val state: StateFlow<State<V, A, S>>
    get() = _state


  private fun log(oldState: State<V, A, S>, state: State<V, A, S>) {
    synchronized(this) {
      val viewS = state.view?.javaClass?.name
      val actionS = state.action?.peekContent()?.javaClass?.name
      val sideEffect = state.sideEffect?.javaClass?.name

      var message = "\n"
      viewS?.let {
        if (oldState.view?.equals(state.view) != true)
          message += "ViewState:  $it\n"
      }
      actionS?.let {
        if (oldState.action?.equals(state.action) != true)
          message += "Action:  $it\n"
      }
      sideEffect?.let {
        if (oldState.sideEffect?.equals(state.sideEffect) != true)
          message += "SideEffect:  $it \n"
      }
      Log.i("State-Machine ${tag}", message)
    }
  }

  private fun moveState(state: State<V, A, S>) {
    log(_state.value, state)
    _state.value = state.copy()

  }

  override fun currentViewState() = _state.value.view ?: throw NullPointerException("State boş")
  override fun currentSideEffect() = _state.value.sideEffect?: throw NullPointerException("Side Effect BOş")


  override fun moveTo(state: V): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(view = state))
  }

  override fun actionTo(action: A): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(action = Event(action)))
  }

  override fun sideEffectTo(sideEffect: S): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(sideEffect = sideEffect))
  }

  override fun moveTo(state: V, action: A): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(view = state, action = Event(action)))
  }

  override fun moveTo(state: V, action: A, sideEffect: S): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(view = state, action = Event(action), sideEffect = sideEffect))
  }


  override fun clearViewState(): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(view = null))
  }

  override fun clearActionState(): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(action = null))
  }

  override fun clearSideEffect(): Reducer<V, A, S> = apply {
    moveState(_state.value.copy(sideEffect = null))
  }
}

