package com.enciyo.pinbook.ui.fav

import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.reducer.RepoState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow


@ExperimentalCoroutinesApi
data class FavoriteRepoState(
    val favoriteBooks: MutableStateFlow<MutableList<PopularBooks>> = MutableStateFlow(mutableListOf())
) : RepoState()