package com.assignment.greencatsoft.adaptor.out.groupUser

import org.springframework.data.jpa.repository.JpaRepository

interface GroupUserRepository : JpaRepository<GroupUserEntity, Long> {
    fun existsByGroupIdAndUserEmail(groupId: Long, userEmail: String): Boolean
}
