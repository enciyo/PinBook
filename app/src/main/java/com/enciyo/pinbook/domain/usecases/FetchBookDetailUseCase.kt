package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.BookDetail
import javax.inject.Inject


class FetchBookDetailUseCase @Inject constructor(
    private val mRepository: PinBookRepository
): BaseSuspendUseCase<Int?,BookDetail>(){
    override suspend fun execute(parameters: Int?): AwesomeResult<BookDetail> =
        mRepository
            .fetchBookDetail(parameters?:0)

}