package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.adaptor.`in`.group.GroupListRes
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<GroupEntity, Long>, GroupQuery

interface GroupQuery {
    fun getGroups(email: String): List<GroupListRes>
}
