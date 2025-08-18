package com.assignment.greencatsoft.application.service.token

import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.token.TokenQueryUseCase
import com.assignment.greencatsoft.application.port.out.token.TokenSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.config.LoginUserDetail
import com.assignment.greencatsoft.config.Role
import com.assignment.greencatsoft.domain.user.User
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.UUID

@Service
@Transactional
class TokenProvider(
    private val getUserPort: UserGetPort,
    private val tokenSavePort: TokenSavePort,
) : TokenQueryUseCase, TokenOperationUseCase {

    private val current get() = LocalDateTime.now(ZoneOffset.of("+8"))
    private val key: Key = Keys.hmacShaKeyFor(
        "a90751ed6690ed27c0d701abbb8cc3192fdad41d01722230ee2ec6adcde69638e5d621d66e992b1f13aa647661311598108af1323b4d12140a035a6487abaff1"
            .toByteArray(Charsets.UTF_8),
    )

    override fun createToken(user: User): Pair<String, String> {
        val accessTokenTime = current.plus(60 * 60 * 24, ChronoUnit.SECONDS)
        val refreshTokenTime = current.plus(60 * 60 * 24 * 365, ChronoUnit.SECONDS)
        val refreshToken = createRefreshToken(user.email, refreshTokenTime)
        val accessToken = createToken(user, accessTokenTime)

        tokenSavePort.addToken(refreshToken, refreshTokenTime, user.email, Role.USER)

        return accessToken to createCookie(refreshToken)
    }

    override fun validateToken(token: String?): Boolean {
        try {
            token?.let {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
                return true
            }
        } catch (e: SecurityException) {
            throw e
        } catch (e: ExpiredJwtException) {
            throw e
        } catch (e: UnsupportedJwtException) {
            throw e
        } catch (e: IllegalArgumentException) {
            throw e
        }
        return false
    }

    override fun resolveToken(request: HttpServletRequest?): String? {
        val token = request?.getHeader("Authorization")

        return token?.let {
            if (token.startsWith("Bearer ")) {
                token.substringAfter("Bearer ")
            } else {
                token
            }
        }
    }

    override fun parseToken(token: String): Map<String, String> {
        val jwt = if (token.startsWith("Bearer ")) {
            token.substring(7)
        } else {
            token
        }

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body
            .entries.associate { (key, value) ->
                key to value.toString()
            }
    }

    override fun getSubAndRole(token: String): Pair<String, Role> {
        val parsed = parseToken(token)
        return Pair(parsed["sub"]!!, Role.valueOf(parsed["role"]!!.uppercase()))
    }

    override fun getAuthentication(token: String): Authentication {
        val (email, role) = getSubAndRole(token)

        val account = when (role) {
            Role.USER -> getUserPort.findByEmail(email)
        }

        val loginUserDetail = LoginUserDetail(account, role)
        return UsernamePasswordAuthenticationToken(loginUserDetail, "", loginUserDetail.authorities)
    }

    private fun createCookie(value: String): String = ResponseCookie
        .from("refresh", value)
        .httpOnly(true)
        .secure(true)
        .maxAge(2592000)
        .sameSite("Lax").path("/")
        .build().toString()

    private fun createToken(user: User, tokenExpireTime: LocalDateTime): String = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setSubject(user.email)
        .claim("name", user.name)
        .claim("role", Role.USER)
        .claim("status", user.status.name)
        .setExpiration(Date(tokenExpireTime.toInstant(ZoneOffset.of("+09:00")).toEpochMilli()))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact()

    private fun createRefreshToken(email: String, tokenExpireTime: LocalDateTime): String = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setSubject(email)
        .claim("type", "refresh")
        .claim("rand", UUID.randomUUID())
        .setExpiration(Date(tokenExpireTime.toInstant(ZoneOffset.of("+09:00")).toEpochMilli()))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact()
}
