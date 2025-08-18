package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.group.GroupImpl
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
object GroupMapper {

    fun toEntity(domain: Group) = GroupEntity(
        owner = domain.owner,
        name = domain.name,
        private = domain.private,
    ).apply {
        this.id = domain.id
    }

    fun toDomain(entity: GroupEntity) = GroupImpl(
        id = entity.id!!,
        owner = entity.owner,
        name = entity.name,
        private = entity.private,
    )

    fun toPersonalGroup(user: User) = GroupEntity(
        owner = user.email,
        name = "개인그룹",
        private = false,
    )
}
