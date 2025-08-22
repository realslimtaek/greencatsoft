package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleSavePort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import com.assignment.greencatsoft.domain.schedule.Schedule
import org.springframework.stereotype.Component
import java.time.YearMonth
import kotlin.jvm.optionals.getOrNull

@Component
class ScheduleRepositoryAdaptor(
    private val repository: ScheduleRepository,
    private val mapper: ScheduleMapper,
) : ScheduleGetPort, ScheduleSavePort {

    override fun addSchedule(req: AddScheduleReq) {
        mapper.toEntity(req)
            .run(repository::save)
    }

    override fun updateSchedule(req: UpdateScheduleReq) {
        mapper.toEntity(req)
    }

    override fun findById(id: Long): Schedule = repository.findById(id).getOrNull()
        ?.run(mapper::toDomain)
        ?: throwError(CustomErrorCode.DataNotFound)

    override fun getSchedules(email: String, ym: YearMonth, groupId: Long?) = repository.getSchedule(email, ym, groupId)

    override fun isMySchedule(email: String, id: Long): Boolean = repository.isMySchedule(email, id)

    override fun save(domain: Schedule) {
        repository.save(mapper.toEntity(domain))
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }
}
