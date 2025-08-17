package com.assignment.greencatsoft.domain.user

import com.assignment.greencatsoft.adaptor.out.user.UserEntity.UsersStatus

interface User {
    var id: Long?
    val email: String
    var password: String
    var name: String?
    var status: UsersStatus
}

data class UserImpl(
    override var id: Long?,
    override val email: String,
    override var password: String,
    override var name: String?,
    override var status: UsersStatus
): User
