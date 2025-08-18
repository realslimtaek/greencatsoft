package com.assignment.greencatsoft.application.port.`in`.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq

interface UserQueryPort

interface UserOperationPort {

    fun signIn(req: UserSignInReq)
}
