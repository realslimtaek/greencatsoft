package com.assignment.greencatsoft.application.port.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.GetScheduleRes
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import com.assignment.greencatsoft.domain.schedule.Schedule
import java.time.YearMonth

interface ScheduleGetPort {
    fun findById(id: Long): Schedule

    fun getSchedules(email: String, ym: YearMonth, groupId: Long?): List<GetScheduleRes>

    fun isMySchedule(email: String, id: Long): Boolean
}

interface ScheduleSavePort {
    fun addSchedule(req: AddScheduleReq)

    fun updateSchedule(req: UpdateScheduleReq)

    fun save(domain: Schedule)

    fun delete(id: Long)
}
