package com.assignment.greencatsoft.application.service.schedule

import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleQueryUseCase
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleSavePort
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val scheduleSavePort: ScheduleSavePort,
    private val scheduleGetPort: ScheduleGetPort,
): ScheduleOperationUseCase, ScheduleQueryUseCase
