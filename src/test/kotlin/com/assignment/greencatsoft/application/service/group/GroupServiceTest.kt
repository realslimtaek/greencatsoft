package com.assignment.greencatsoft.application.service.group

import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupRes
import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import com.assignment.greencatsoft.domain.group.Group
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class GroupServiceTest {
    private lateinit var groupService: GroupService
    private lateinit var groupGetPort: GroupGetPort
    private lateinit var groupSavePort: GroupSavePort
    private lateinit var groupUserSavePort: GroupUserSavePort
    private lateinit var resMapper: GroupResMapper

    @BeforeEach
    fun setUp() {
        groupGetPort = mock()
        groupSavePort = mock()
        groupUserSavePort = mock()
        resMapper = mock()
        groupService = GroupService(
            groupGetPort,
            groupSavePort,
            groupUserSavePort,
            resMapper,
        )
    }

    val baseOwner = "asdf@asdf.com"
    val baseName = "가족여행"

    val baseUser = "asdf@asdf2.com"

    private val mockOwnerGroup: Group = mock {
        on { id } doReturn 1L
        on { owner } doReturn baseOwner
        on { name } doReturn baseName
        on { private } doReturn false
    }

    private val mockOtherGroup: Group = mock {
        on { id } doReturn 2L
        on { owner } doReturn baseUser
        on { name } doReturn "부산여행"
        on { private } doReturn false
    }

    @Test
    fun `그룹 생성 로직 테스트`() {
        val req = object : GroupAddReq {
            override val owner = baseOwner
            override val name = baseName
        }

        val mockResponse: DefaultGroupRes = mock {
            on { id } doReturn 1L
            on { owner } doReturn req.owner
            on { name } doReturn req.name
        }

        whenever(groupSavePort.makeGroup(req)).thenReturn(mockOwnerGroup)
        whenever(resMapper.toBaseResponse(mockOwnerGroup)).thenReturn(mockResponse)

        val result = groupService.addGroup(req)

        verify(groupSavePort).makeGroup(req)
        verify(groupUserSavePort).makeOwnGroupUser(mockOwnerGroup.id!!, mockOwnerGroup.owner)
        verify(resMapper).toBaseResponse(mockOwnerGroup)

        assertEquals(mockResponse, result)
    }

    @Test
    fun `그룹 소유자 삭제 테스트`() {
        whenever(groupGetPort.findById(any())).thenReturn(mockOwnerGroup)

        groupService.deleteGroup(baseOwner, 1L)

        verify(groupGetPort).findById(any())
        verify(groupSavePort).deleteGroup(any())
    }

    @Test
    fun `그룹 일원 삭제 테스트`() {
        whenever(groupGetPort.findById(any())).thenReturn(mockOwnerGroup)

        groupService.deleteGroup(baseUser, 2L)

        verify(groupGetPort).findById(any())
        verify(groupUserSavePort).resignGroup(2L, baseUser)
    }
}
