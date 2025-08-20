package com.assignment.greencatsoft.application.port.`in`.groupUser

import com.assignment.greencatsoft.adaptor.`in`.group.user.GroupInviteReq

interface GroupUserQueryUseCase

interface GroupUserOperationUseCase {
    fun inviteGroup(req: GroupInviteReq)

    fun updateGroupInvite(email: String, groupId: Long)
}
