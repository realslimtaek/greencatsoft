package com.assignment.greencatsoft.domain.group

interface Group {
    val id: Long
    val name: String
    val private: Boolean
}

data class GroupImpl(
    override val id: Long,
    override val name: String,
    override val private: Boolean,
) : Group
