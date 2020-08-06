package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.common.map
import com.enciyo.pinbook.domain.FlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val mRepository: PinBookRepository
) : FlowUseCase<LoginUseCase.LoginUseCaseParameters, LoginUseCase.LoginUseCaseResult>() {


  data class LoginUseCaseParameters(val username: String, val password: String)
  data class LoginUseCaseResult(val user: User)

  override fun execute(parameters: LoginUseCaseParameters?): Flow<AwesomeResult<LoginUseCaseResult>> =
      mRepository.fetchLogin(parameters!!.username, parameters.password)
          .map {
            it.map {
              LoginUseCaseResult(it)
            }
          }

}