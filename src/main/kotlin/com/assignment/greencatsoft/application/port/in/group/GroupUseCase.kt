package com.assignment.greencatsoft.application.port.`in`.group

import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupRes
import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq
import com.assignment.greencatsoft.adaptor.`in`.group.GroupListRes

interface GroupQueryUseCase {
    fun getGroups(email: String): List<GroupListRes>
}

interface GroupOperationUseCase {
    fun addGroup(req: GroupAddReq): DefaultGroupRes
}
