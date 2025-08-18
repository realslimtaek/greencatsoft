package com.assignment.greencatsoft.application.service.group

import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupRes
import com.assignment.greencatsoft.adaptor.`in`.group.DefaultGroupResDto
import com.assignment.greencatsoft.domain.group.Group
import org.springframework.stereotype.Component

@Component
object GroupResMapper {

    fun toBaseResponse(group: Group): DefaultGroupRes = DefaultGroupResDto(
        id = group.id!!,
        owner = group.owner,
        name = group.name,
    )
}
