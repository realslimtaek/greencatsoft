package com.assignment.greencatsoft.adaptor.out.token

import com.assignment.greencatsoft.config.Role
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
object TokenMapper {

    fun toEntity(refreshToken: String, expired: LocalDateTime, id: String, role: Role) = TokenEntity(
        uuid = id,
        role = role,
        refreshToken = refreshToken,
        refreshExpireAt = expired,
    )
}
