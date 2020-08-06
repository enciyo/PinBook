package com.enciyo.pinbook.reducer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow


@ExperimentalCoroutinesApi
abstract class HasReducerViewModel<V : ViewState, A : ActionState,  S : SideEffect>(private val reducer: Reducer<V,A,S>) : ViewModel(), Reducer<V,A,S>{

  override val state: StateFlow<State<V, A, S>>
    get() = reducer.state

  override fun currentViewState(): V = reducer.currentViewState()
  override fun currentSideEffect(): S = reducer.currentSideEffect()

  override fun moveTo(state: V): Reducer<V, A, S> = reducer.moveTo(state)

  override fun actionTo(action: A): Reducer<V, A, S> = reducer.actionTo(action)

  override fun sideEffectTo(sideEffect: S): Reducer<V, A, S> = reducer.sideEffectTo(sideEffect)

  override fun moveTo(state: V, action: A): Reducer<V, A, S> = reducer.moveTo(state)

  override fun moveTo(state: V, action: A, sideEffect: S): Reducer<V, A, S> = reducer.moveTo(state)

  override fun clearViewState(): Reducer<V, A, S> = reducer.clearViewState()

  override fun clearActionState(): Reducer<V, A, S> = reducer.clearActionState()

  override fun clearSideEffect(): Reducer<V, A, S> = reducer.clearSideEffect()
}