package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.BaseSuspendUseCase
import com.enciyo.pinbook.domain.model.User
import javax.inject.Inject


class FetchCurrentUserUseCase @Inject constructor(
    private val mRepository: PinBookRepository
): BaseSuspendUseCase<Nothing?, User>() {

  override suspend fun execute(parameters: Nothing?): AwesomeResult<User> {
    return mRepository.fetchCurrentUser()
  }

}