package com.assignment.greencatsoft.application.port.out.groupUser

interface GroupUserSavePort {
    fun makePrivateGroupUser(groupId: Long, userid: Long)
}

interface GroupUserGetPort
