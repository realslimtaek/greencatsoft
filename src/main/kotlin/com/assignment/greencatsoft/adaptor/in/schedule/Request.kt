package com.assignment.greencatsoft.adaptor.`in`.schedule

import java.time.LocalDate
import java.time.LocalTime

interface AddScheduleReq {
    val groupId: Long
    val title: String?
    val startDate: LocalDate
    val startTime: LocalTime?
    val endDate: LocalDate
    val endTime: LocalTime?
    val memo: String?
    val writer: String
}

interface UpdateScheduleReq : AddScheduleReq {
    val id: Long
}
