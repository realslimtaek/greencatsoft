package com.assignment.greencatsoft.adaptor.`in`.schedule

import java.time.LocalDate
import java.time.LocalTime

interface GetScheduleRes {
    val id: Long?
    val groupName: String?
    val writerName: String?
    val title: String?
    val startDate: LocalDate?
    val startTime: LocalTime?
    val endDate: LocalDate?
    val endTime: LocalTime?
    val memo: String?
}
