package com.enciyo.pinbook.ui.books.state

import com.enciyo.pinbook.common.RepoWrapper
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.Category
import com.enciyo.pinbook.reducer.RepoState


data class BookRepoState(
    val categories: RepoWrapper<List<Category>> = RepoWrapper(mutableListOf()),
    val books: RepoWrapper<List<Book>> = RepoWrapper(mutableListOf()),
    val filteredBooks: RepoWrapper<List<Book>> = RepoWrapper(mutableListOf())
) : RepoState()