package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseFlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchBooksUseCase @Inject constructor(
    private val mRepository: PinBookRepository
): BaseFlowUseCase<String?,List<Book>>(){

  override fun execute(parameters: String?): Flow<AwesomeResult<List<Book>>> =
      mRepository
          .fetchFilteredBooks(parameters)

}