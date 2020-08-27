package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import okhttp3.ResponseBody
import javax.inject.Inject


class RateBookUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : BaseSuspendUseCase<RateBookUseCase.RateBookUseCaseParams, ResponseBody>() {

    data class RateBookUseCaseParams(
        val comment:String,
        val bookId:String,
        val rating:Double
    )

    override suspend fun execute(parameters: RateBookUseCaseParams?): AwesomeResult<ResponseBody> =
        mRepository.fetchRate(
            bookID = parameters!!.bookId.toInt(),
            comment = parameters.comment,
            rating = parameters.rating
        )

}