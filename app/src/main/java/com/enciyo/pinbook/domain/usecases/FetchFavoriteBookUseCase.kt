package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.PopularBooks
import javax.inject.Inject


class FetchFavoriteBookUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : BaseSuspendUseCase<String?, PopularBooks>() {

    override suspend fun execute(parameters: String?): AwesomeResult<PopularBooks>  =
        mRepository.getFavoriteBook(parameters)
}