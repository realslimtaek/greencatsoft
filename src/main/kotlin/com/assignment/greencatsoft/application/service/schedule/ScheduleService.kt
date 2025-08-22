package com.assignment.greencatsoft.application.service.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.AddScheduleReq
import com.assignment.greencatsoft.adaptor.`in`.schedule.UpdateScheduleReq
import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleQueryUseCase
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleGetPort
import com.assignment.greencatsoft.application.port.out.schedule.ScheduleSavePort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
@Transactional
class ScheduleService(
    private val scheduleSavePort: ScheduleSavePort,
    private val scheduleGetPort: ScheduleGetPort,
    private val groupUserGetPort: GroupUserGetPort,
) : ScheduleOperationUseCase, ScheduleQueryUseCase {

    override fun deleteSchedule(email: String, id: Long) {
        if (scheduleGetPort.isMySchedule(email, id)) {
            scheduleSavePort.delete(id)
        } else {
            throwError(CustomErrorCode.NotMySchedule)
        }
    }

    override fun addSchedule(req: AddScheduleReq) {
        if (groupUserGetPort.amIInGroup(req.groupId, req.writer)) {
            scheduleSavePort.addSchedule(req)
        } else {
            throwError(CustomErrorCode.NotMyGroup)
        }
    }

    override fun updateSchedule(req: UpdateScheduleReq) {
        scheduleGetPort.findById(req.id)
            .apply {
                this.groupId = req.groupId
                this.title = req.title
                this.startDate = req.startDate
                this.startTime = req.startTime
                this.endDate = req.endDate
                this.endTime = req.endTime
                this.memo = req.memo
                this.writer = req.writer
            }
            .run(scheduleSavePort::save)
    }

    override fun getSchedule(email: String, ym: YearMonth, groupId: Long?) = scheduleGetPort.getSchedules(email, ym, groupId)
}
