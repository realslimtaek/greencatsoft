package com.assignment.greencatsoft.domain.groupUser

interface GroupUser {
    val id: Long?
    val groupId: Long
    val userEmail: String
    var accepted: Boolean
}

data class GroupUserImpl(
    override val id: Long?,
    override val groupId: Long,
    override val userEmail: String,
    override var accepted: Boolean,
) : GroupUser
