package com.assignment.greencatsoft.domain.schedule

import java.time.LocalDate
import java.time.LocalTime

interface Schedule {
    val id: Long?
    var groupId: Long
    var title: String?
    var startDate: LocalDate
    var startTime: LocalTime?
    var endDate: LocalDate
    var endTime: LocalTime?
    var memo: String?
    var writer: String
}

data class ScheduleImpl(
    override val id: Long?,
    override var groupId: Long,
    override var title: String?,
    override var startDate: LocalDate,
    override var startTime: LocalTime?,
    override var endDate: LocalDate,
    override var endTime: LocalTime?,
    override var memo: String?,
    override var writer: String,
) : Schedule
