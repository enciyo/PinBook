package com.enciyo.pinbook.reducer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

/**
 * @author mustafa kilic
 */
@ExperimentalCoroutinesApi
interface Reducer<V : ViewState, A : ActionState, S : SideEffect> {

  val state: StateFlow<State<V, A, S>>

  fun currentViewState(): V
  fun currentSideEffect(): S

  fun moveTo(state: V): Reducer<V, A, S>
  fun moveTo(state: V, action: A): Reducer<V, A, S>
  fun moveTo(state: V, action: A, sideEffect: S): Reducer<V, A, S>

  fun actionTo(action: A): Reducer<V, A, S>
  fun sideEffectTo(sideEffect: S): Reducer<V, A, S>


  fun clearViewState(): Reducer<V, A, S>
  fun clearActionState(): Reducer<V, A, S>
  fun clearSideEffect(): Reducer<V, A, S>

}