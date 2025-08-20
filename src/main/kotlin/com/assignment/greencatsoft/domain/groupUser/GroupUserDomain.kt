package com.assignment.greencatsoft.domain.groupUser

interface GroupUser {
    val id: Long?
    val groupId: Long
    val userId: Long
    var accepted: Boolean
}

data class GroupUserImpl(
    override val id: Long?,
    override val groupId: Long,
    override val userId: Long,
    override var accepted: Boolean,
) : GroupUser
