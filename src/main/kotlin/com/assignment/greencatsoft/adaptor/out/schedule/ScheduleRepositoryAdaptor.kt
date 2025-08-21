package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.application.port.out.schedule.ScheduleGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleSavePort
import org.springframework.stereotype.Component

@Component
class ScheduleRepositoryAdaptor(
    private val repository: ScheduleRepository,
): ScheduleGetPort, ScheduleSavePort {
}
