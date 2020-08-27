package com.enciyo.pinbook.ui.detail

import com.enciyo.pinbook.common.RepoWrapper
import com.enciyo.pinbook.domain.model.BookDetail
import com.enciyo.pinbook.reducer.RepoState


data class BookDetailRepoState(
    val bookDetail: RepoWrapper<BookDetail> = RepoWrapper(BookDetail.empty()),
    val commentResult: RepoWrapper<String> = RepoWrapper("")
) : RepoState()