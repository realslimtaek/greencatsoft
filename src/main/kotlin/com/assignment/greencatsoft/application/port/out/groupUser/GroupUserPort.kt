package com.assignment.greencatsoft.application.port.out.groupUser

import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User

interface GroupUserSavePort {
    fun makeOwnGroupUser(groupId: Long, userEmail: String)

    fun inviteUser(group: Group, user: User)
}

interface GroupUserGetPort {
    fun checkExistsUser(groupId: Long, userEmail: String): Boolean
}
