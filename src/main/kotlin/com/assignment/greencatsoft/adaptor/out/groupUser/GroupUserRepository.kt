package com.assignment.greencatsoft.adaptor.out.groupUser

import org.springframework.data.jpa.repository.JpaRepository

interface GroupUserRepository : JpaRepository<GroupUserEntity, Long> {
    fun existsByGroupIdAndUserEmail(groupId: Long, userEmail: String): Boolean

    fun findByGroupIdAndUserEmailAndAcceptedIsFalse(groupId: Long, userEmail: String): GroupUserEntity?

    fun deleteByGroupIdAndUserEmailAndAcceptedIsFalse(groupId: Long, userEmail: String)

    fun deleteByGroupIdAndUserEmailAndAcceptedIsTrue(groupId: Long, userEmail: String)

    fun existsByGroupIdAndUserEmailAndAcceptedIsTrue(groupId: Long, userEmail: String): Boolean
}
