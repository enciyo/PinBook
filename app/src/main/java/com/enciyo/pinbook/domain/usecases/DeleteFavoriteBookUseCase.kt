package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.PopularBooks
import javax.inject.Inject


class DeleteFavoriteBookUseCase @Inject constructor(
    val mRepository: PinBookRepository
)  : BaseSuspendUseCase<PopularBooks, Boolean>() {

    override suspend fun execute(parameters: PopularBooks?): AwesomeResult<Boolean> {
        if (parameters == null){
            throw NullPointerException("$parameters must be not null")
        }
        return mRepository.deleteFavorite(parameters)
    }

}