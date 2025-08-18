package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReqDto
import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest {

    private lateinit var userSavePort: UserSavePort
    private lateinit var userGetPort: UserGetPort
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var tokenOperationUseCase: TokenOperationUseCase
    private lateinit var userService: UserService
    private lateinit var groupSavePort: GroupSavePort
    private lateinit var responseMapper: UserResponseMapper

    @BeforeEach
    fun setUp() {
        userSavePort = mock()
        userGetPort = mock()
        passwordEncoder = mock()
        tokenOperationUseCase = mock()
        groupSavePort = mock()
        responseMapper = mock()
        userService = UserService(userSavePort, userGetPort, groupSavePort, passwordEncoder, tokenOperationUseCase, responseMapper)
    }

    @Test
    fun `회원가입 비즈니스 로직 테스트`() {
        // given
        val req = UserSignInReqDto(
            email = "test@example.com",
            password = "Password123!",
            rePassword = "Password123!",
        )

        // No need to specify doNothing if just normal behavior
        doNothing().`when`(userGetPort).checkExistsEmail(req.email)
        doNothing().`when`(userSavePort).save(req)

        // when
        userService.signIn(req)

        // then
        verify(userGetPort).checkExistsEmail(req.email)
        verify(userSavePort).save(req)
    }
}
