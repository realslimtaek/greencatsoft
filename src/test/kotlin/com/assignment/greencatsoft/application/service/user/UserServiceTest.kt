package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UpdateUserInfoReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserInfoResDto
import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReqDto
import com.assignment.greencatsoft.adaptor.out.user.UserEntity.UserStatus
import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User
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
    private lateinit var groupUserSavePort: GroupUserSavePort

    @BeforeEach
    fun setUp() {
        userSavePort = mock()
        userGetPort = mock()
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        tokenOperationUseCase = mock()
        groupSavePort = mock()
        responseMapper = mock()
        groupUserSavePort = mock()
        userService = UserService(
            userSavePort,
            userGetPort,
            groupSavePort,
            passwordEncoder,
            tokenOperationUseCase,
            responseMapper,
            groupUserSavePort,
        )
    }

    val defaultMockEmail = "test@example.com"
    val defaultMockPassword = "encodedPassword1!"

    val mockPendingUser: User = mock {
        on { id } doReturn 1L
        on { email } doReturn defaultMockEmail
        on { password } doReturn defaultMockPassword
        on { name } doReturn null
        on { status } doReturn UserStatus.PENDING
    }

    @Test
    fun `회원가입 비즈니스 로직 테스트`() {
        val req = UserSignInReqDto(defaultMockEmail, defaultMockPassword, defaultMockPassword)

        doNothing().`when`(userGetPort).checkExistsEmail(req.email)

        val mockGroup: Group = mock {
            on { id } doReturn 1L
            on { owner } doReturn req.email
            on { name } doReturn "홍길동"
            on { private } doReturn true
        }

        whenever(userSavePort.save(req)).thenReturn(mockPendingUser)

        whenever(groupSavePort.makePersonalGroup(mockPendingUser)).thenReturn(mockGroup)

        userService.signIn(req)

        verify(userGetPort).checkExistsEmail(req.email)
        verify(userSavePort).save(req)

        val captor = argumentCaptor<User>()
        verify(groupSavePort).makePersonalGroup(captor.capture())

        val savedUser = captor.firstValue
        assertEquals(req.email, savedUser.email)
        assertEquals(req.password, savedUser.password)
        assertEquals(1L, savedUser.id)
        assertNull(savedUser.name)
        assertEquals(UserStatus.PENDING, savedUser.status)
    }

    @Test
    fun `사용자 정보 수정`() {
        val req = object : UpdateUserInfoReq {
            override val email = defaultMockEmail
            override val name = "홍길동"
        }

        val updatedUser = mockPendingUser.apply {
            this.name = req.name
            this.status = UserStatus.ACTIVE
        }

        val expectedRes = UserInfoResDto(
            id = updatedUser.id!!,
            email = updatedUser.email,
            name = updatedUser.name,
            status = updatedUser.status.name,
        )

        whenever(userGetPort.findByEmail(req.email)).thenReturn(mockPendingUser)
        whenever(userSavePort.save(any<User>())).thenReturn(updatedUser)
        whenever(responseMapper.toUpdateRes(updatedUser)).thenReturn(expectedRes)

        val actualRes = userService.updateInfo(req)

        verify(userGetPort).findByEmail(req.email)
        verify(userSavePort).save(mockPendingUser)
        verify(responseMapper).toUpdateRes(updatedUser)

        assertEquals(expectedRes, actualRes)
    }
}
