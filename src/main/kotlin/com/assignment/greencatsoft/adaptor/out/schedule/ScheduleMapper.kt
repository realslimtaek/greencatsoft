package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.domain.schedule.Schedule
import com.assignment.greencatsoft.domain.schedule.ScheduleImpl
import org.springframework.stereotype.Component

@Component
object ScheduleMapper {

    fun toEntity(req: AddScheduleReq) = ScheduleEntity(
        groupId = req.groupId,
        title = req.title,
        startDate = req.startDate,
        startTime = req.startTime,
        endDate = req.endDate,
        endTime = req.endTime,
        memo = req.memo,
        writer = req.writer,
    )

    fun toDomain(entity: ScheduleEntity) = ScheduleImpl(
        id = entity.id,
        groupId = entity.groupId,
        title = entity.title,
        startDate = entity.startDate,
        startTime = entity.startTime,
        endDate = entity.endDate,
        endTime = entity.endTime,
        memo = entity.memo,
        writer = entity.writer,
    )

    fun toEntity(domain: Schedule) = ScheduleEntity(
        groupId = domain.groupId,
        title = domain.title,
        startDate = domain.startDate,
        startTime = domain.startTime,
        endDate = domain.endDate,
        endTime = domain.endTime,
        memo = domain.memo,
        writer = domain.writer,
    ).apply {
        this.id = domain.id
    }
}
