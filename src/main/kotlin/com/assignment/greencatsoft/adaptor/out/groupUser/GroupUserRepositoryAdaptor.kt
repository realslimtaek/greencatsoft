package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import org.springframework.stereotype.Component

@Component
class GroupUserRepositoryAdaptor(
    private val repository: GroupUserRepository,
    private val groupUserMapper: GroupUserMapper,
) : GroupUserSavePort, GroupUserGetPort {

    override fun makePrivateGroupUser(groupId: Long, userid: Long) {
        groupUserMapper.toPersonalEntity(groupId, userid)
            .run(repository::save)
    }
}
