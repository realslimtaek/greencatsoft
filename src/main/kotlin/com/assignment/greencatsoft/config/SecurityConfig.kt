package com.assignment.greencatsoft.config

import com.assignment.greencatsoft.adaptor.out.user.UserEntity
import com.assignment.greencatsoft.application.service.token.TokenProvider
import com.assignment.greencatsoft.domain.user.User
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val tokenProvider: TokenProvider,
) {
    @Bean
    fun filterChain(http: HttpSecurity) = http
        .csrf { it.disable() }
        .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } } // H2 콘솔 사용을 위한 설정
        .authorizeHttpRequests {
            it.requestMatchers("/v1/parents/**").hasRole("USER")
                .anyRequest().permitAll() // 그 외의 모든 요청은 인증 x
        }
        .exceptionHandling {
            it.accessDeniedHandler { _, response, _ ->
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.contentType = "application/json"
                response.writer.write("""{"message": "Access Denied"}""")
            }
            it.authenticationEntryPoint { _, response, _ ->
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("""{"message": "Unauthorized"}""")
            }
        }
        .addFilterBefore(LoginFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        } // 세션을 사용하지 않으므로 STATELESS 설정
        .build()!!

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}

class LoginFilter(
    private val tokenService: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = tokenService.resolveToken(request)

        try {
            if (token != null && tokenService.validateToken(token)) {
                SecurityContextHolder.getContext().authentication = tokenService.getAuthentication(token)
            }
        } catch (_: ExpiredJwtException) {
            setErrorResponse(response, CustomErrorCode.TokenExpired)
            return
        } catch (_: Exception) {
            setErrorResponse(response, CustomErrorCode.InvalidToken)
            return
        }
        chain.doFilter(request, response)
    }
}

private fun setErrorResponse(response: HttpServletResponse, error: ErrorCode) {
    response.status = error.statusCode
    response.contentType = "application/json;charset=UTF-8"
    response.writer.write(
        """
        {
            "classify": "${error.code}",
            "message": "${error.label}",
        }
        """.trimIndent(),
    )
}

class LoginUserDetail(
    val user: User,
    val role: Role,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(role.name))

    override fun getPassword(): String? = password

    override fun getUsername(): String? = user.name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = user.status == UserEntity.UserStatus.RESIGN

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = user.status != UserEntity.UserStatus.RESIGN
}
