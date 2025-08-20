package com.assignment.greencatsoft.application.service.groupUser

import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserQueryUseCase
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserGetPort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
import org.springframework.stereotype.Service

@Service
class GroupUserService(
    private val groupUserSavePort: GroupUserSavePort,
    private val groupUserGetPort: GroupUserGetPort,
) : GroupUserQueryUseCase, GroupUserOperationUseCase
