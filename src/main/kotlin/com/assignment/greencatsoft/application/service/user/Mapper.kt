package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.LoginResDto
import com.assignment.greencatsoft.adaptor.`in`.user.UserInfoResDto
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
object UserResponseMapper {

    fun toUpdateRes(domain: User) = UserInfoResDto(
        id = domain.id!!,
        email = domain.email,
        name = domain.name,
        status = domain.status.name,
    )

    fun toLoginRes(token: String, cookie: String, user: User) = LoginResDto(
        token = token,
        cookie = cookie,
        res = toUpdateRes(user),
    )
}
