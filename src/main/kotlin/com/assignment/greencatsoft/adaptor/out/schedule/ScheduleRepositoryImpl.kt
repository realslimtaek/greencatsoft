package com.assignment.greencatsoft.adaptor.out.schedule

import com.assignment.greencatsoft.adaptor.`in`.schedule.GetScheduleRes
import com.assignment.greencatsoft.adaptor.out.group.QGroupEntity.groupEntity
import com.assignment.greencatsoft.adaptor.out.groupUser.QGroupUserEntity.groupUserEntity
import com.assignment.greencatsoft.adaptor.out.schedule.QScheduleEntity.scheduleEntity
import com.assignment.greencatsoft.adaptor.out.user.QUserEntity.userEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Repository
class ScheduleRepositoryImpl(
    private val factory: JPAQueryFactory,
) : QuerydslRepositorySupport(ScheduleEntity::class.java), ScheduleQuery {

    override fun getSchedule(email: String, ym: YearMonth, groupId: Long?): List<GetScheduleRes> {
        val standardStart = ym.atDay(1)
        val standardEnd = ym.atEndOfMonth()

        return factory
            .select(
                Projections.fields(
                    GetScheduleResDto::class.java,
                    scheduleEntity.id,
                    groupEntity.id.`as`("groupId"),
                    groupEntity.name.`as`("groupName"),
                    userEntity.name.`as`("writerName"),
                    scheduleEntity.title,
                    scheduleEntity.startDate,
                    scheduleEntity.startTime,
                    scheduleEntity.endDate,
                    scheduleEntity.endTime,
                    scheduleEntity.memo,
                ),
            )
            .from(scheduleEntity)
            .join(groupEntity)
            .on(
                BooleanBuilder().apply {
                    this.and(groupEntity.id.eq(scheduleEntity.groupId))
                    groupId?.let { this.and(groupEntity.id.eq(it)) }
                },
            )
            .join(groupUserEntity)
            .on(
                groupUserEntity.groupId.eq(groupEntity.id),
                groupUserEntity.accepted.isTrue,
            )
            .join(userEntity)
            .on(
                userEntity.email.eq(groupUserEntity.userEmail),
                userEntity.email.eq(email),
            )
            .where(
                BooleanBuilder().apply {
                    this.or(
                        BooleanBuilder().apply {
                            this.and(scheduleEntity.startDate.loe(standardStart))
                            this.and(scheduleEntity.endDate.goe(standardEnd))
                        },
                    )
                    this.or(
                        BooleanBuilder().apply {
                            this.and(scheduleEntity.startDate.goe(standardStart))
                            this.and(scheduleEntity.endDate.loe(standardEnd))
                        },
                    )
                },
            )
            .orderBy(
                scheduleEntity.startDate.asc(),
                scheduleEntity.startTime.asc(),
                scheduleEntity.endDate.asc(),
                scheduleEntity.endTime.asc(),
            )
            .fetch()
    }

    override fun isMySchedule(email: String, id: Long): Boolean {
        val res = factory
            .select(scheduleEntity.id)
            .from(scheduleEntity)
            .join(groupUserEntity)
            .on(
                groupUserEntity.groupId.eq(scheduleEntity.groupId),
                groupUserEntity.userEmail.eq(email),
            )
            .where(scheduleEntity.id.eq(id))
            .limit(1)
            .fetchOne()

        return res != null
    }
}

data class GetScheduleResDto(
    override val id: Long? = null,
    override val groupId: Long? = null,
    override val groupName: String? = null,
    override val writerName: String? = null,
    override val title: String? = null,
    override val startDate: LocalDate? = null,
    override val startTime: LocalTime? = null,
    override val endDate: LocalDate? = null,
    override val endTime: LocalTime? = null,
    override val memo: String? = null,
) : GetScheduleRes
