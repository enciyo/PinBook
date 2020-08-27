package com.enciyo.pinbook.reducer

import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author mustafa kilic
 */
@ExperimentalCoroutinesApi
interface Reducer<V : ViewState, A : ActionState, S : RepoState> {

  val state: StateFlow<State<V, A, S>>
  val viewState: StateFlow<V?>
  val repoState: MutableStateFlow<S?>
  val actionState: StateFlow<Event<A>?>

  fun currentViewState(): V
  fun currentActionState(): A?
  fun currentRepoState(): S?


  fun viewTo(view: V): Reducer<V, A, S>
  fun actionTo(action: A): Reducer<V, A, S>
  fun repoTo(repo: (S) -> Unit): Reducer<V, A, S>
  fun stateTo(view: V, action: A, repo: (S) -> Unit): Reducer<V, A, S>


  fun clearViewState(): Reducer<V, A, S>
  fun clearActionState(): Reducer<V, A, S>
  fun clearSideEffect(): Reducer<V, A, S>


}









