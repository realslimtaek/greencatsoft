package com.assignment.greencatsoft.application.port.`in`.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserLoginReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq

interface UserQueryUseCase {
    fun login(req: UserLoginReq): Pair<String, String>
}

interface UserOperationUseCase {

    fun signIn(req: UserSignInReq)
}
