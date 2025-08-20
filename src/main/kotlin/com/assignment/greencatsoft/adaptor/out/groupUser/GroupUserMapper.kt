package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.groupUser.GroupUser
import com.assignment.greencatsoft.domain.groupUser.GroupUserImpl
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
object GroupUserMapper {

    fun toEntity(domain: GroupUser) = GroupUserEntity(
        groupId = domain.groupId,
        userEmail = domain.userEmail,
        accepted = domain.accepted,
    ).apply {
        this.id = domain.id
    }

    fun toPersonalEntity(groupId: Long, userEmail: String) = GroupUserEntity(
        groupId = groupId,
        userEmail = userEmail,
        accepted = true,
    )

    fun toEntity(group: Group, user: User) = GroupUserEntity(
        groupId = group.id!!,
        userEmail = user.email,
        accepted = false,
    )

    fun toDomain(entity: GroupUserEntity) = GroupUserImpl(
        id = entity.id,
        groupId = entity.groupId,
        userEmail = entity.userEmail,
        accepted = entity.accepted,
    )
}
