package com.assignment.greencatsoft.domain.user

import com.assignment.greencatsoft.adaptor.out.user.UserEntity.UserStatus

interface User {
    var id: Long?
    val email: String
    var password: String
    var name: String?
    var status: UserStatus
}

data class UserImpl(
    override var id: Long?,
    override val email: String,
    override var password: String,
    override var name: String?,
    override var status: UserStatus,
) : User
