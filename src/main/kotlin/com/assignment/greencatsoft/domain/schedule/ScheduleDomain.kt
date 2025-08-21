package com.assignment.greencatsoft.domain.schedule

import java.time.LocalDate
import java.time.LocalTime

interface Schedule {
    val groupId: Long
    var title: String?
    var startDate: LocalDate
    var startTime: LocalTime?
    var endDate: LocalDate
    var endTime: LocalTime?
    var memo: String?
}

data class ScheduleImpl(
    override val groupId: Long,
    override var title: String?,
    override var startDate: LocalDate,
    override var startTime: LocalTime?,
    override var endDate: LocalDate,
    override var endTime: LocalTime?,
    override var memo: String?
): Schedule
