package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.group.GroupImpl
import org.springframework.stereotype.Component

@Component
object GroupMapper {

    fun toEntity(domain: Group) = GroupEntity(
        name = domain.name,
        private = domain.private,
    ).apply {
        this.id = domain.id
    }

    fun toDomain(entity: GroupEntity) = GroupImpl(
        id = entity.id!!,
        name = entity.name,
        private = entity.private,
    )
}
