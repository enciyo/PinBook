package com.enciyo.pinbook.reducer

import android.util.Log
import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * @author kilicmustafa
 */
@ExperimentalCoroutinesApi
class ReducerImp<V : ViewState, A : ActionState, S : RepoState>(private val tag: String? = null, private val a: State<V, A, S>) : Reducer<V, A, S> {

  companion object {
    inline fun <reified V : ViewState, reified A : ActionState, reified S : RepoState> create(tag: String? = null): Reducer<V, A, S> {
      return ReducerImp<V, A, S>(tag, State.create<V, A, S>())
    }

    inline fun <reified V : ViewState, reified A : ActionState, reified S : RepoState> createNonInt(tag: String? = null): ReducerImp<V, A, S> {
      return ReducerImp<V, A, S>(tag, State.create<V, A, S>())
    }
  }


  private val _actionState: MutableStateFlow<Event<A>?> = MutableStateFlow(a.action)
  override val actionState: StateFlow<Event<A>?>
    get() = _actionState

  private val _viewState = MutableStateFlow(a.view)
  override val viewState: StateFlow<V?>
    get() = _viewState

  private val _repoState = MutableStateFlow(a.repoState)
  override val repoState: MutableStateFlow<S?>
    get() = _repoState


  private val _state: MutableStateFlow<State<V, A, S>> = MutableStateFlow(a)
  override val state: StateFlow<State<V, A, S>>
    get() = _state


  private fun log(oldState: State<V, A, S>, state: State<V, A, S>) {

    synchronized(this) {
      val viewS = state.view?.javaClass?.name
      val actionS = state.action?.peekContent()?.javaClass?.name
      val sideEffect = state.repoState?.javaClass?.name

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
        if (oldState.repoState?.equals(state.repoState) != true)
          message += "RepoState:  $it \n"
      }
      Log.i("State-Machine ${tag}", message)
    }
  }

  private fun moveState(state: State<V, A, S>) {
    log(_state.value, state)
    _state.value = state.copy()

  }

  override fun currentViewState() = _state.value.view ?: throw NullPointerException("View null")
  override fun currentRepoState(): S? = _state.value.repoState
  override fun currentActionState(): A? = actionState.value?.peekContent()


  override fun viewTo(view: V): Reducer<V, A, S> = apply {
    _viewState.value = view
    moveState(_state.value.copy(view = view))
  }


  override fun actionTo(action: A): Reducer<V, A, S> = apply {
    _actionState.value = Event(action)
    moveState(_state.value.copy(action = Event(action)))
  }

  override fun stateTo(view: V, action: A, repo: (S) -> Unit): Reducer<V, A, S> = apply {
    viewTo(view)
    actionTo(action)
    repoTo(repo)
  }

  override fun repoTo(repo: (S) -> Unit) = apply {
    repo.invoke(_repoState.value!!)
    repo.invoke(_state.value.copy().repoState!!)
  }


  override fun clearViewState(): Reducer<V, A, S> = apply {
    _viewState.value = null
    moveState(_state.value.copy(view = null))
  }

  override fun clearActionState(): Reducer<V, A, S> = apply {
    _actionState.value = null
    moveState(_state.value.copy(action = null))
  }

  override fun clearSideEffect(): Reducer<V, A, S> = apply {
    _repoState.value = null
    moveState(_state.value.copy(repoState = null))
  }


}

