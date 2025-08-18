package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReqDto
import com.assignment.greencatsoft.adaptor.out.user.UserEntity
import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import com.assignment.greencatsoft.domain.user.User
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.kotlin.*
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

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
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        tokenOperationUseCase = mock()
        groupSavePort = mock()
        responseMapper = mock()
        userService = UserService(userSavePort, userGetPort, groupSavePort, passwordEncoder, tokenOperationUseCase, responseMapper)
    }

    @Test
    fun `회원가입 비즈니스 로직 테스트`() {
        // given
        val req = UserSignInReqDto("test@example.com", "Password123!", "Password123!")

        doNothing().`when`(userGetPort).checkExistsEmail(req.email)

        // save() 호출 시 mock User를 반환
        val mockUser: User = mock {
            on { id } doReturn 1L
            on { email } doReturn req.email
            on { password } doReturn passwordEncoder.encode(req.password)
            on { name } doReturn null
            on { status } doReturn UserEntity.UserStatus.PENDING
        }

        whenever(userSavePort.save(req)).thenReturn(mockUser)

        doNothing().`when`(groupSavePort).makePersonalGroup(any())

        // when
        userService.signIn(req)

        // then
        verify(userGetPort).checkExistsEmail(req.email)
        verify(userSavePort).save(req)

        // groupSavePort 호출 시 전달된 User 검증
        val captor = argumentCaptor<User>()
        verify(groupSavePort).makePersonalGroup(captor.capture())

        val savedUser = captor.firstValue
        assertEquals(req.email, savedUser.email)
        assertTrue(passwordEncoder.matches(req.password, savedUser.password))
        assertEquals(1L, savedUser.id)
        assertNull(savedUser.name)
        assertEquals(UserEntity.UserStatus.PENDING, savedUser.status)
    }
}
