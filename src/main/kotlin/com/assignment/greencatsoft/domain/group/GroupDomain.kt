package com.assignment.greencatsoft.domain.group

interface Group {
    val id: Long?
    val owner: String
    var name: String
    val private: Boolean
}

data class GroupImpl(
    override val id: Long?,
    override val owner: String,
    override var name: String,
    override val private: Boolean,
) : Group
