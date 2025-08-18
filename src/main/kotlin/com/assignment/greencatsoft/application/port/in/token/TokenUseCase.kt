package com.assignment.greencatsoft.application.port.`in`.token

import com.assignment.greencatsoft.config.Role
import com.assignment.greencatsoft.domain.user.User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication

interface TokenQueryUseCase {
    fun validateToken(token: String?): Boolean
    fun resolveToken(request: HttpServletRequest?): String?
    fun parseToken(token: String): Map<String, String>
    fun getSubAndRole(token: String): Pair<String, Role>
    fun getAuthentication(token: String): Authentication
}

interface TokenOperationUseCase {
    fun createToken(user: User): Pair<String, String>
}
