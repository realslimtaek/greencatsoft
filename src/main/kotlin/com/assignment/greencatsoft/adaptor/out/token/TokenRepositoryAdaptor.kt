package com.assignment.greencatsoft.adaptor.out.token

import com.assignment.greencatsoft.application.port.out.token.TokenGetPort
import com.assignment.greencatsoft.application.port.out.token.TokenSavePort
import com.assignment.greencatsoft.config.Role
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TokenRepositoryAdaptor(
    private val tokenRepository: TokenRepository,
    private val mapper: TokenMapper,
) : TokenSavePort, TokenGetPort {
    override fun addToken(refreshToken: String, expired: LocalDateTime, email: String, role: Role) {
        mapper.toEntity(refreshToken, expired, email, role)
            .run(tokenRepository::save)
    }
}
