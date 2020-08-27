package com.enciyo.pinbook.reducer


import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@ExperimentalCoroutinesApi
fun <V : ViewState, A : ActionState, S : RepoState> StateFlow<State<V, A, S>>.render(
    onState: (V) -> Unit,
    onAction: (A) -> Unit,
    onSideEffect: (S) -> Unit,
    savedStateHandle: Bundle? = null
): Flow<State<V, A, S>> {
    var oneTimeEmit = false
    if (savedStateHandle != null)
        oneTimeEmit = false
    return this
        .onStart { emit(this@render.value.copy()) }
        .onEach {
            logSt("", it)
            it.view?.let(onState)
            it.action?.getContentIfNotHandled()?.let(onAction)
            if (oneTimeEmit.not()) {
                it.repoState?.let(onSideEffect)
                oneTimeEmit = true
            }

        }
}

@ExperimentalCoroutinesApi
fun <V : ViewState, A : ActionState, S : RepoState> ReducerImp<V, A, S>.render(
    onState: (V) -> Unit,
    onAction: (A) -> Unit,
    onRepo: (S) -> Unit,
    lifecycle: LifecycleOwner
) {
    viewState
        .onEach { it?.let(onState) }.launchIn(lifecycle.lifecycleScope)
    actionState
        .onEach { it?.getContentIfNotHandled()?.let(onAction) }.launchIn(lifecycle.lifecycleScope)
    repoState
        .onEach { it?.let(onRepo) }.launchIn(lifecycle.lifecycleScope)

}

@ExperimentalCoroutinesApi
fun <V : ViewState, A : ActionState, S : RepoState> Reducer<V, A, S>.render(
    lifecycle: LifecycleOwner,
    onState: (V) -> Unit,
    onRepo: (S) -> Unit,
    onActionIfNotHandled: ((A) -> Unit)? = null,
    onAction: ((A) -> Unit)? = null
) {
    viewState
        .onEach { view -> view?.let(onState) }.launchIn(lifecycle.lifecycleScope)


    actionState
        .onEach { event ->
            onActionIfNotHandled?.let { event?.peekContent()?.let(it) }
            onAction?.let { event?.getContentIfNotHandled()?.let(it) }
        }.launchIn(lifecycle.lifecycleScope)


    repoState
        .onEach { repo -> repo?.let(onRepo) }.launchIn(lifecycle.lifecycleScope)

}


@ExperimentalCoroutinesApi
class ReducerProperty<in T : ViewModel, V : ViewState, A : ActionState, S : RepoState>(
    val tag: String? = null,
    val state: State<V, A, S>
) : ReadOnlyProperty<T, Reducer<V, A, S>> {

    var reducer: Reducer<V, A, S>? = null

    override fun getValue(thisRef: T, property: KProperty<*>): Reducer<V, A, S> {
        reducer?.let { return it }
        return ReducerImp<V, A, S>(tag, state).also {
            reducer = it
        }
    }

}

@ExperimentalCoroutinesApi
@Suppress("unused")
inline fun <reified V : ViewState, A : ActionState, reified S : RepoState> ViewModel.reducer(): ReducerProperty<ViewModel, V, A, S> {
    return ReducerProperty(this.javaClass.simpleName, State.create())
}


private fun <V : ViewState, A : ActionState, S : RepoState> logSt(
    tag: String?,
    state: State<V, A, S>
) {
    val viewS = state.view?.javaClass?.name
    val actionS = state.action?.peekContent()?.javaClass?.name
    val sideEffect = state.repoState?.javaClass?.name

    var message = "----------- \n"
    viewS?.let {
        message += "ViewState:  $it\n"
    }
    actionS?.let {
        message += "Action hasBeenHandled ${state.action?.hasBeenHandled} : $it \n"
    }
    sideEffect?.let {
        message += "RepoState:  $it \n"
    }
    message += "-----------"
    Log.i("State-Collector ${tag}", message)
}



