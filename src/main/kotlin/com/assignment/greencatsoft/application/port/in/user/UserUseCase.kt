package com.assignment.greencatsoft.application.port.`in`.user

import com.assignment.greencatsoft.adaptor.`in`.user.LoginRes
import com.assignment.greencatsoft.adaptor.`in`.user.UpdateUserInfoReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserInfoRes
import com.assignment.greencatsoft.adaptor.`in`.user.UserLoginReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq

interface UserQueryUseCase {
    fun login(req: UserLoginReq): LoginRes
}

interface UserOperationUseCase {

    fun signIn(req: UserSignInReq)

    fun updateInfo(req: UpdateUserInfoReq): UserInfoRes
}
