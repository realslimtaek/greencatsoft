package com.assignment.greencatsoft.application.port.out.group

import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq
import com.assignment.greencatsoft.adaptor.`in`.group.GroupListRes
import com.assignment.greencatsoft.domain.group.Group
import com.assignment.greencatsoft.domain.user.User

interface GroupSavePort {
    fun makePersonalGroup(user: User): Group
    fun makeGroup(req: GroupAddReq): Group
}

interface GroupGetPort {
    fun findById(id: Long): Group
    fun getGroups(email: String): List<GroupListRes>
}
