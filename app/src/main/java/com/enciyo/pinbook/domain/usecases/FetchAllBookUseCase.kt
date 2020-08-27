package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseFlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FetchAllBookUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : BaseFlowUseCase<Nothing?,List<Book>>(){

  override fun execute(parameters: Nothing?): Flow<AwesomeResult<List<Book>>> = mRepository.fetchBooks()

}