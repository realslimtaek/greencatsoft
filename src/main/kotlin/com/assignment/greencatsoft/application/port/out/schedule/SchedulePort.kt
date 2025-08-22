package com.assignment.greencatsoft.application.port.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import com.assignment.greencatsoft.domain.schedule.Schedule
import java.time.YearMonth

interface ScheduleGetPort {
    fun findById(id: Long): Schedule

    fun getSchedule(email: String, ym: YearMonth, groupId: Long?)
}

interface ScheduleSavePort {
    fun addSchedule(req: AddScheduleReq)

    fun updateSchedule(req: UpdateScheduleReq)

    fun save(domain: Schedule)
}
