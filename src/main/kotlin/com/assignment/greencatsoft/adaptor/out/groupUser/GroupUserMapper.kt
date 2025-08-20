package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
object GroupUserMapper {

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
}
