package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq
import com.assignment.greencatsoft.adaptor.`in`.group.GroupListRes
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class GroupRepositoryAdaptor(
    private val groupRepository: GroupRepository,
    private val groupMapper: GroupMapper,
) : GroupSavePort, GroupGetPort {

    override fun makePersonalGroup(user: User): Group = groupMapper.toPersonalGroup(user)
        .run(groupRepository::save)
        .run(groupMapper::toDomain)

    override fun makeGroup(req: GroupAddReq): Group = groupMapper.toEntity(req)
        .run(groupRepository::save)
        .run(groupMapper::toDomain)

    override fun findById(id: Long): Group = groupRepository.findById(id)
        .getOrNull()
        ?.run(groupMapper::toDomain)
        ?: throwError(CustomErrorCode.NotFoundGroup)

    override fun deleteGroup(groupId: Long) {
        groupRepository.deleteById(groupId)
    }

    override fun getGroups(email: String): List<GroupListRes> = groupRepository.getGroups(email)
}
