package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReqDto
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify

class UserServiceTest {

    private lateinit var userSavePort: UserSavePort
    private lateinit var userGetPort: UserGetPort
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userSavePort = mock()
        userGetPort = mock()
        userService = UserService(userSavePort, userGetPort)
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
