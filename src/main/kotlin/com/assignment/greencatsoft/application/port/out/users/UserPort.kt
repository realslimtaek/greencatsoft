package com.assignment.greencatsoft.application.port.out.users

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq
import com.assignment.greencatsoft.domain.user.User

interface UserGetPort {
    fun checkExistsEmail(email: String)

    fun findByEmail(email: String): User
}

interface UserSavePort {
    fun save(req: UserSignInReq): User
    fun save(domain: User): User
}
