package com.enciyo.pinbook.ui.dashboard.states

import com.enciyo.pinbook.common.RepoWrapper
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import com.enciyo.pinbook.reducer.RepoState


data class DashboardRepoState(
    val popularBooks: RepoWrapper<MutableList<PopularBooks>> = RepoWrapper(mutableListOf()),
    val showcaseBooks: RepoWrapper<MutableList<ShowcaseBooks>> = RepoWrapper(mutableListOf())
) : RepoState()