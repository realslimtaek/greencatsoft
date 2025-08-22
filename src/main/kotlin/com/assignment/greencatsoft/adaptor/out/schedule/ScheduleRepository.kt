package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.GetScheduleRes
import org.springframework.data.jpa.repository.JpaRepository
import java.time.YearMonth

interface ScheduleRepository : JpaRepository<ScheduleEntity, Long>

interface ScheduleQuery {
    fun getSchedule(email: String, ym: YearMonth, groupId: Long?): List<GetScheduleRes>
}
