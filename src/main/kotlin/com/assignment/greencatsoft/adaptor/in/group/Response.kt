package com.assignment.greencatsoft.adaptor.`in`.group

interface DefaultGroupRes {
    val id: Long
    val owner: String
    val name: String
}

data class DefaultGroupResDto(
    override val id: Long,
    override val owner: String,
    override val name: String,
) : DefaultGroupRes

interface GroupListRes {
    val id: Long
    val name: String
    val isOwner: Boolean
}
