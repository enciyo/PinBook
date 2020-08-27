package com.enciyo.pinbook.domain.usecases

import com.enciyo.pinbook.common.AwesomeResult
import com.enciyo.pinbook.domain.BaseFlowUseCase
import com.enciyo.pinbook.domain.PinBookRepository
import com.enciyo.pinbook.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
    val mRepository: PinBookRepository
): BaseFlowUseCase<RegisterUseCase.RegisterUseCaseParams, User>() {


    data class RegisterUseCaseParams(
       val username: String,
       val password: String,
       val displayName: String,
       val email: String
    )

    override fun execute(parameters: RegisterUseCaseParams?): Flow<AwesomeResult<User>> = flow{
        val response = mRepository
            .fetchRegister(
                username = parameters!!.username,
                password = parameters.password,
                displayName = parameters.displayName,
                email = parameters.email
            )
        emit(response)
    }
}