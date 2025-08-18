package com.assignment.greencatsoft.application.port.out.token

import com.assignment.greencatsoft.config.Role
import java.time.LocalDateTime

interface TokenGetPort

interface TokenSavePort {
    fun addToken(refreshToken: String, expired: LocalDateTime, id: String, role: Role)
}
