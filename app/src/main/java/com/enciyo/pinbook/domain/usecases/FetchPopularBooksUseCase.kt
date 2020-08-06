package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.mapFlowList
import com.enciyo.pinbook.domain.FlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.mapper.toPopularBook
import com.enciyo.pinbook.domain.model.PopularBooks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FetchPopularBooksUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : FlowUseCase<Nothing?, List<PopularBooks>>() {


  override fun execute(parameters: Nothing?): Flow<AwesomeResult<List<PopularBooks>>> {
    return mRepository.getBooks()
        .mapFlowList {
          it.toPopularBook()
        }
  }
}



