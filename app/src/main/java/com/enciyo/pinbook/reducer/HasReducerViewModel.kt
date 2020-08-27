package com.enciyo.pinbook.reducer

import androidx.lifecycle.ViewModel
import com.enciyo.pinbook.utils.livedata.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@ExperimentalCoroutinesApi
abstract class HasReducerViewModel<V : ViewState, A : ActionState, S : RepoState>(private val reducer: Reducer<V, A, S>) :
    ViewModel(), Reducer<V, A, S> {


    override val viewState: StateFlow<V?>
        get() = reducer.viewState

    override val repoState: MutableStateFlow<S?>
        get() = reducer.repoState

    override val actionState: StateFlow<Event<A>?>
        get() = reducer.actionState


    override val state: StateFlow<State<V, A, S>>
        get() = reducer.state

    override fun currentViewState(): V = reducer.currentViewState()
    override fun currentActionState(): A? = reducer.currentActionState()
    override fun currentRepoState(): S? = reducer.currentRepoState()

    override fun viewTo(view: V): Reducer<V, A, S> = reducer.viewTo(view)
    override fun actionTo(action: A): Reducer<V, A, S> = reducer.actionTo(action)
    override fun repoTo(repo: (S) -> Unit) = reducer.repoTo(repo)

    override fun stateTo(view: V, action: A, repo: (S) -> Unit): Reducer<V, A, S> =
        reducer.stateTo(view, action, repo)


    override fun clearViewState(): Reducer<V, A, S> = reducer.clearViewState()
    override fun clearActionState(): Reducer<V, A, S> = reducer.clearActionState()
    override fun clearSideEffect(): Reducer<V, A, S> = reducer.clearSideEffect()
}