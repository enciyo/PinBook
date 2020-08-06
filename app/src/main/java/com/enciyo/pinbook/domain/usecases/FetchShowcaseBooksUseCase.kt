package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.map
import com.enciyo.pinbook.domain.FlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.ShowcaseBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FetchShowcaseBooksUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : FlowUseCase<Nothing?, List<ShowcaseBooks>>() {

  override fun execute(parameters: Nothing?): Flow<AwesomeResult<List<ShowcaseBooks>>> = mRepository.fetchShowcaseBooks()
      .map {
        it.map {
          it.takeLast(5)
        }
      }



}