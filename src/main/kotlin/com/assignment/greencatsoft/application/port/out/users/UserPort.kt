package com.assignment.greencatsoft.application.port.out.users

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq

interface UserGetPort {
    fun checkExistsEmail(email: String)
}

interface UserSavePort {
    fun save(req: UserSignInReq)
}
