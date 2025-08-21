package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.adaptor.out.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "SCHEDULE")
@Comment("일정")
class ScheduleEntity(

    @Column(name = "GROUP_ID", columnDefinition = "bigint(11)", nullable = false)
    val groupId: Long,

    @Column(name = "TITLE", columnDefinition = "varchar(30)")
    var title: String?,

    @Column(name = "START_DATE", columnDefinition = "date", nullable = false)
    @Comment("일정 시작 일자")
    var startDate: LocalDate,

    @Column(name = "START_TIME", columnDefinition = "time", nullable = true)
    @Comment("일정 시작 시각 | NULL = 하루 종일 일정")
    var startTime: LocalTime?,

    @Column(name = "END_DATE", columnDefinition = "date", nullable = false)
    @Comment("일정 종료 일자")
    var endDate: LocalDate,

    @Column(name = "END_TIME", columnDefinition = "time", nullable = true)
    @Comment("일정 종료 시각 | NULL = 하루 종일 일정")
    var endTime: LocalTime?,

    @Column(name = "MEMO", columnDefinition = "text", nullable = true)
    @Comment("일정에 대한 추가 메모")
    var memo: String? = null,


) : BaseEntity()
