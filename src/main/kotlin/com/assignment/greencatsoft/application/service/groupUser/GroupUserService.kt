package com.assignment.greencatsoft.application.service.groupUser

import com.assignment.greencatsoft.adaptor.`in`.group.user.GroupInviteReq
import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserQueryUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class GroupUserService(
    private val groupUserSavePort: GroupUserSavePort,
    private val groupUserGetPort: GroupUserGetPort,
    private val groupGetPort: GroupGetPort,
    private val userGetPort: UserGetPort,
) : GroupUserQueryUseCase, GroupUserOperationUseCase {

    override fun inviteGroup(req: GroupInviteReq) {
        val group = groupGetPort.findById(req.groupId)
        if (group.owner != req.owner) {
            throwError(CustomErrorCode.NotGroupOwner)
        }

        if(group.private) {
            throwError(CustomErrorCode.NotPublicGroup)
        }

        if (groupUserGetPort.checkExistsUser(group.id!!, req.email)) {
            throwError(CustomErrorCode.UserAlreadyInvited)
        }

        val invited = userGetPort.findByEmail(req.email)

        groupUserSavePort.inviteUser(group, invited)
    }

    override fun acceptGroupInvite(email: String, groupId: Long) {
        groupUserGetPort.getInviteHistory(groupId, email)
            .apply { this.accepted = true }
            .run(groupUserSavePort::save)
    }

    override fun declineGroupInvite(email: String, groupId: Long) {
        groupUserGetPort.getInviteHistory(groupId, email)
            .apply { this.accepted = true }
            .run(groupUserSavePort::delete)
    }
}
