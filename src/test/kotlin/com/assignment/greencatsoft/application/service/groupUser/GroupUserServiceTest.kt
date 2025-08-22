package com.assignment.greencatsoft.application.service.groupUser

import com.assignment.greencatsoft.adaptor.`in`.group.user.GroupInviteReq
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.groupUser.GroupUser
import com.assignment.greencatsoft.domain.user.User
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class GroupUserServiceTest {
    private lateinit var groupUserService: GroupUserService
    private lateinit var groupUserSavePort: GroupUserSavePort
    private lateinit var groupUserGetPort: GroupUserGetPort
    private lateinit var groupGetPort: GroupGetPort
    private lateinit var userGetPort: UserGetPort

    @BeforeEach
    fun setUp() {
        groupUserSavePort = mock()
        groupUserGetPort = mock()
        groupGetPort = mock()
        userGetPort = mock()
        groupUserService = GroupUserService(
            groupUserSavePort,
            groupUserGetPort,
            groupGetPort,
            userGetPort,
        )
    }

    private val baseOwnerEmail = "owner@example.com"
    private val baseInvitedEmail = "invited@example.com"
    private val baseGroupId = 1L
    private val baseGroupUserId = 100L

    private val mockOwnerGroup: Group = mock {
        on { id } doReturn baseGroupId
        on { owner } doReturn baseOwnerEmail
        on { name } doReturn "Test Group"
    }

    private val mockInvitedUser: User = mock {
        on { id } doReturn 2L
        on { email } doReturn baseInvitedEmail
    }

    private val mockInviteHistory: GroupUser = object : GroupUser {
        override val id: Long = baseGroupUserId
        override val groupId: Long = baseGroupId
        override val userEmail: String = baseInvitedEmail
        override var accepted: Boolean = false
    }

    @Test
    fun `그룹초대 테스트`() {
        // Arrange
        val req = object : GroupInviteReq {
            override val groupId = baseGroupId
            override val owner = baseOwnerEmail
            override val email = baseInvitedEmail
            override fun validate() {
                doNothing()
            }
        }

        whenever(groupGetPort.findById(req.groupId)).thenReturn(mockOwnerGroup)
        whenever(groupUserGetPort.checkExistsUser(req.groupId, req.email)).thenReturn(false)
        whenever(userGetPort.findByEmail(req.email)).thenReturn(mockInvitedUser)

        // Act
        groupUserService.inviteGroup(req)

        // Assert
        verify(groupUserSavePort).inviteUser(mockOwnerGroup, mockInvitedUser)
    }

    @Test
    fun `그룹초대수락 테스트`() {
        // Arrange
        whenever(groupUserGetPort.getInviteHistory(baseGroupId, baseInvitedEmail)).thenReturn(mockInviteHistory)

        // Act
        groupUserService.acceptGroupInvite(baseInvitedEmail, baseGroupId)

        // Assert
        assertTrue(mockInviteHistory.accepted)
        verify(groupUserSavePort).save(mockInviteHistory)
    }

    @Test
    fun `그룹초대거절 테스트`() {
        // Arrange
        whenever(groupUserGetPort.getInviteHistory(baseGroupId, baseInvitedEmail)).thenReturn(mockInviteHistory)

        // Act
        groupUserService.declineGroupInvite(baseInvitedEmail, baseGroupId)

        // Assert
        verify(groupUserSavePort).delete(mockInviteHistory)
    }
}
