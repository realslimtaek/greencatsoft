package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.application.port.out.group.GroupGetPort
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import org.springframework.stereotype.Component

@Component
class GroupRepositoryAdaptor(
    private val groupRepository: GroupRepository,
) : GroupSavePort, GroupGetPort
