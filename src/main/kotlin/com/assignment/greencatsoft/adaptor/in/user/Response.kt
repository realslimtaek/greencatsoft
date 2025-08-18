package com.assignment.greencatsoft.adaptor.`in`.user

interface UserInfoRes {
    val id: Long
    val email: String
    val name: String?
    val status: String
}

data class UserInfoResDto(
    override val id: Long,
    override val email: String,
    override val name: String?,
    override val status: String,
) : UserInfoRes

interface LoginRes {
    val token: String
    val cookie: String
    val res: UserInfoRes
}

data class LoginResDto(
    override val token: String,
    override val cookie: String,
    override val res: UserInfoResDto,
) : LoginRes
