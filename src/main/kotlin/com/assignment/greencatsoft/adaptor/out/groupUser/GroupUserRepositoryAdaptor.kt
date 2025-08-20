package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import org.springframework.stereotype.Repository

@Repository
class GroupUserRepositoryAdaptor(
    private val repository: GroupUserRepository
): GroupUserSavePort, GroupUserGetPort {
}
