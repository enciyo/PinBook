package com.enciyo.pinbook.reducer


import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@ExperimentalCoroutinesApi
fun <V : ViewState, A : ActionState, S : SideEffect> StateFlow<State<V, A, S>>.render(
    onState: (V) -> Unit,
    onAction: (A) -> Unit,
    onSideEffect: (S) -> Unit
): Flow<State<V, A, S>> {
  var isLastEmitted = false

  return this
      .onEach {
        logSt("", it)
        it.view?.let(onState)
        if (isLastEmitted.not()){
          it.sideEffect?.let(onSideEffect)
          isLastEmitted = true
        }

        it.action?.getContentIfNotHandled()?.let(onAction)
      }

}


@ExperimentalCoroutinesApi
class ReducerProperty<in T : ViewModel, V : ViewState, A : ActionState, S : SideEffect>(val tag: String? = null,val state: State<V,A,S>) : ReadOnlyProperty<T, Reducer<V, A, S>> {

  var reducer: Reducer<V, A, S>? = null

  override fun getValue(thisRef: T, property: KProperty<*>): Reducer<V, A, S> {
    reducer?.let { return it }
    return ReducerImp<V, A, S>(tag,state).also {
      reducer = it
    }
  }

}

@ExperimentalCoroutinesApi
@Suppress("unused")
inline fun <reified V : ViewState, A : ActionState, reified S : SideEffect> ViewModel.reducer(): ReducerProperty<ViewModel, V, A, S> {
  return ReducerProperty(this.javaClass.simpleName,State.create())
}


private fun <V : ViewState, A : ActionState, S : SideEffect> logSt(tag: String?, state: State<V, A, S>) {
  val viewS = state.view?.javaClass?.name
  val actionS = state.action?.peekContent()?.javaClass?.name
  val sideEffect = state.sideEffect?.javaClass?.name

  var message = "----------- \n"
  viewS?.let {
    message += "ViewState:  $it\n"
  }
  actionS?.let {
    message += "Action hasBeenHandled ${state.action?.hasBeenHandled} : $it \n"
  }
  sideEffect?.let {
    message += "SideEffect:  $it \n"
  }
  message += "-----------"
  Log.i("State-Collector ${tag}", message)
}



