package com.enciyo.pinbook.reducer


import com.enciyo.pinbook.utils.livedata.Event


/**
 * @author kilicmustafa
 */
data class State<V : ViewState, A : ActionState, out S : RepoState>(val view: V? = null, val action: Event<A>? = null, val repoState: S? = null) {

  companion object {
    inline fun <reified V : ViewState, A : ActionState, reified S : RepoState> create() =
        State<V, A, S>(V::class.java.newInstance(), null, S::class.java.newInstance())
  }
}

