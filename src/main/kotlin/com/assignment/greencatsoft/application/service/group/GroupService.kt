package com.assignment.greencatsoft.application.service.group

import com.assignment.greencatsoft.application.port.`in`.group.GroupOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.group.GroupQueryUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class GroupService(
    private val groupGetPort: GroupGetPort,
    private val groupSavePort: GroupSavePort,
) : GroupOperationUseCase, GroupQueryUseCase
