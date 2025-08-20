package com.assignment.greencatsoft.application.port.out.groupUser

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.groupUser.GroupUser
import com.assignment.greencatsoft.domain.user.User

interface GroupUserSavePort {
    fun makeOwnGroupUser(groupId: Long, userEmail: String)

    fun inviteUser(group: Group, user: User)

    fun save(domain: GroupUser)

    fun delete(domain: GroupUser)
}

interface GroupUserGetPort {
    fun checkExistsUser(groupId: Long, userEmail: String): Boolean

    fun getInviteHistory(groupId: Long, email: String): GroupUser
}
