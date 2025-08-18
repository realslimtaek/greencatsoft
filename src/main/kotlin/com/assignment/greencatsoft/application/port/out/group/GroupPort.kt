package com.assignment.greencatsoft.application.port.out.group

import com.assignment.greencatsoft.domain.user.User

interface GroupSavePort {
    fun makePersonalGroup(user: User)
}

interface GroupGetPort
