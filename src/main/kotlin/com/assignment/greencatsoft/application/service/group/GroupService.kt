package com.assignment.greencatsoft.application.service.group

import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupRes
import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq
import com.assignment.greencatsoft.application.port.`in`.group.GroupOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.group.GroupQueryUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class GroupService(
    private val groupGetPort: GroupGetPort,
    private val groupSavePort: GroupSavePort,
    private val groupUserSavePort: GroupUserSavePort,
    private val resMapper: GroupResMapper,
) : GroupOperationUseCase, GroupQueryUseCase {

    override fun addGroup(req: GroupAddReq): DefaultGroupRes = groupSavePort.makeGroup(req)
        .also { groupUserSavePort.makeOwnGroupUser(it.id!!, req.owner) }
        .run(resMapper::toBaseResponse)

    override fun deleteGroup(email: String, groupId: Long) {
        val group = groupGetPort.findById(groupId)
        if (group.owner == email) {
            groupSavePort.deleteGroup(groupId)
        } else {
            groupUserSavePort.resignGroup(groupId, email)
        }
    }

    override fun getGroups(email: String) = groupGetPort.getGroups(email)
}
