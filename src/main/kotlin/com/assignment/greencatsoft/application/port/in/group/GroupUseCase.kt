package com.assignment.greencatsoft.application.port.`in`.group

import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupRes
import com.assignment.greencatsoft.adaptor.`in`.group.GroupAddReq

interface GroupQueryUseCase

interface GroupOperationUseCase {
    fun addGroup(req: GroupAddReq): DefaultGroupRes
}
