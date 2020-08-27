package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.model.Category
import javax.inject.Inject


class FetchBookCategories  @Inject constructor(
    private val mRepository: PinBookRepository
) : BaseSuspendUseCase<Nothing?,List<Category>>(){


  override suspend fun execute(parameters: Nothing?): AwesomeResult<List<Category>> = mRepository.fetchCategories()

}