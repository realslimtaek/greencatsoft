package com.assignment.greencatsoft.adaptor.out.groupUser

import org.springframework.stereotype.Component

@Component
object GroupUserMapper {

    fun toPersonalEntity(groupId: Long, userId: Long) = GroupUserEntity(
        groupId = groupId,
        userId = userId,
        accepted = true,
    )
}
