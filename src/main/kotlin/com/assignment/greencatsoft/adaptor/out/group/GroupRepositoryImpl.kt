package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.adaptor.`in`.group.GroupListRes
import com.assignment.greencatsoft.adaptor.out.group.QGroupEntity.*
import com.assignment.greencatsoft.adaptor.out.groupUser.QGroupUserEntity.*
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class GroupRepositoryImpl(
    private val factory: JPAQueryFactory,
) : QuerydslRepositorySupport(GroupEntity::class.java), GroupQuery {

    override fun getGroups(email: String): List<GroupListRes> = factory
        .select(
            Projections.fields(
                GroupListResDto::class.java,
                groupEntity.id,
                groupEntity.name,
                groupEntity.owner.`as`("_ownerEmail"),
                groupUserEntity.userEmail.`as`("_userEmail"),
            ),
        )
        .from(groupEntity)
        .join(groupUserEntity)
        .on(
            groupUserEntity.userEmail.eq(email),
            groupEntity.id.eq(groupUserEntity.id),
        )
//        .where(groupEntity.)
        .fetch()
}

data class GroupListResDto(
    override val id: Long = -1,
    override val name: String = "",
    private val _ownerEmail: String = "1",
    private val _userEmail: String = "2",
) : GroupListRes {
    override val isOwner: Boolean get() = _ownerEmail == _userEmail
}
