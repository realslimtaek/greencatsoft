package com.assignment.greencatsoft.adaptor.out.user

import com.assignment.greencatsoft.domain.user.User
import com.assignment.greencatsoft.domain.user.UserImpl
import org.springframework.stereotype.Component

@Component
object UsersMapper {

    fun toDomain(entity: UserEntity): User = UserImpl(
        id = entity.id,
        email = entity.email,
        password = entity.password,
        name = entity.name,
        status = entity.status,
    )

    fun toEntity(domain: User) = UserEntity(
        email = domain.email,
        password = domain.password,
        name = domain.name,
        status = domain.status,
    ).apply {
        this.id = domain.id
    }
}
