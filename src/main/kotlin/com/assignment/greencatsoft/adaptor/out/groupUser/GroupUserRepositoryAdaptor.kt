package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
class GroupUserRepositoryAdaptor(
    private val repository: GroupUserRepository,
    private val groupUserMapper: GroupUserMapper,
) : GroupUserSavePort, GroupUserGetPort {

    override fun makeOwnGroupUser(groupId: Long, userEmail: String) {
        groupUserMapper.toPersonalEntity(groupId, userEmail)
            .run(repository::save)
    }

    override fun inviteUser(group: Group, user: User) {
        groupUserMapper.toEntity(group, user)
            .run(repository::save)
    }

    override fun checkExistsUser(groupId: Long, userEmail: String): Boolean = repository.existsByGroupIdAndUserEmail(groupId, userEmail)
}
