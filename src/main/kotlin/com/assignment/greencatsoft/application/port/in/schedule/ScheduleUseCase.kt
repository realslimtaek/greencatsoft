package com.assignment.greencatsoft.application.port.`in`.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.GetScheduleRes
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import java.time.YearMonth

interface ScheduleQueryUseCase {
    fun getSchedule(email: String, ym: YearMonth, groupId: Long?): List<GetScheduleRes>
}

interface ScheduleOperationUseCase {

    fun addSchedule(req: AddScheduleReq)

    fun updateSchedule(req: UpdateScheduleReq)
}
