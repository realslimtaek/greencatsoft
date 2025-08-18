package com.assignment.greencatsoft.adaptor.`in`.user

interface UserSignInReq {
    val email: String
    val password: String
    val rePassword: String
    fun validate()
}

interface UserLoginReq {
    val email: String
    val password: String
}

interface UpdateUserInfoReq {
    val email: String
    val name: String
}
