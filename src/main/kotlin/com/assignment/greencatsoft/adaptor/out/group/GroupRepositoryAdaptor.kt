package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
class GroupRepositoryAdaptor(
    private val groupRepository: GroupRepository,
    private val groupMapper: GroupMapper,
) : GroupSavePort, GroupGetPort {

    override fun makePersonalGroup(user: User) {
        groupMapper.toPersonalGroup(user)
            .run(groupRepository::save)
    }
}
