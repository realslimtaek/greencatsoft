package com.assignment.greencatsoft.application.service.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.GetScheduleRes
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleSavePort
import com.assignment.greencatsoft.domain.schedule.Schedule
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.verify
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals

class ScheduleServiceTest {
    private lateinit var scheduleService: ScheduleService
    private lateinit var scheduleSavePort: ScheduleSavePort
    private lateinit var scheduleGetPort: ScheduleGetPort
    private lateinit var groupUserGetPort: GroupUserGetPort

    @BeforeEach
    fun setUp() {
        scheduleSavePort = mock()
        scheduleGetPort = mock()
        groupUserGetPort = mock()
        scheduleService = ScheduleService(
            scheduleSavePort,
            scheduleGetPort,
            groupUserGetPort,
        )
    }

    private val baseEmail = "test@example.com"
    private val baseGroupId = 1L
    private val baseScheduleId = 2L
    private val baseTitle = "여행 일정"

    private val mockGetScheduleRes: GetScheduleRes = mock {
        on { id } doReturn baseScheduleId
        on { groupId } doReturn baseGroupId
        on { groupName } doReturn "테스트 그룹"
        on { writerName } doReturn "테스트 유저"
        on { title } doReturn baseTitle
        on { startDate } doReturn LocalDate.of(2025, 1, 1)
        on { startTime } doReturn LocalTime.of(10, 0)
        on { endDate } doReturn LocalDate.of(2025, 1, 3)
        on { endTime } doReturn LocalTime.of(18, 0)
        on { memo } doReturn "메모"
    }

    @Test
    fun `일정 삭제 테스트`() {
        whenever(scheduleGetPort.isMySchedule(baseEmail, baseScheduleId)).thenReturn(true)

        scheduleService.deleteSchedule(baseEmail, baseScheduleId)

        verify(scheduleSavePort).delete(baseScheduleId)
    }

    @Test
    fun `일정 등록 테스트`() {
        val req = object : AddScheduleReq {
            override val groupId = baseGroupId
            override val title = baseTitle
            override val writer = baseEmail
            override val startDate = LocalDate.of(2025, 1, 1)
            override val startTime = LocalTime.of(10, 0)
            override val endDate = LocalDate.of(2025, 1, 3)
            override val endTime = LocalTime.of(18, 0)
            override val memo = "메모"
        }
        whenever(groupUserGetPort.amIInGroup(req.groupId, req.writer)).thenReturn(true)

        scheduleService.addSchedule(req)

        verify(scheduleSavePort).addSchedule(req)
    }

    @Test
    fun `일정 수정 테스트`() {
        val mockSchedule: Schedule = mock {
            on { id } doReturn baseScheduleId
            on { groupId } doReturn baseGroupId
            on { title } doReturn baseTitle
            on { startDate } doReturn LocalDate.of(2025, 1, 1)
            on { startTime } doReturn LocalTime.of(10, 0)
            on { endDate } doReturn LocalDate.of(2025, 1, 3)
            on { endTime } doReturn LocalTime.of(18, 0)
            on { memo } doReturn "기존 메모"
            on { writer } doReturn baseEmail
        }

        val req = object : UpdateScheduleReq {
            override val id = baseScheduleId
            override val groupId = baseGroupId
            override val title = "수정된 제목"
            override val startDate = LocalDate.of(2025, 1, 1)
            override val startTime = LocalTime.of(10, 0)
            override val endDate = LocalDate.of(2025, 1, 3)
            override val endTime = LocalTime.of(18, 0)
            override val memo = "수정된 메모"
            override val writer = baseEmail
        }
        whenever(scheduleGetPort.findById(req.id)).thenReturn(mockSchedule)

        scheduleService.updateSchedule(req)

        verify(scheduleSavePort).save(mockSchedule)
    }

    @Test
    fun `일정 조회 테스트`() {
        val ym = YearMonth.of(2025, 1)
        val mockScheduleList = listOf(mockGetScheduleRes)

        whenever(scheduleGetPort.getSchedules(baseEmail, ym, null)).thenReturn(mockScheduleList)

        val result = scheduleService.getSchedule(baseEmail, ym, null)

        verify(scheduleGetPort).getSchedules(baseEmail, ym, null)
        assertEquals(mockScheduleList, result)
    }
}
