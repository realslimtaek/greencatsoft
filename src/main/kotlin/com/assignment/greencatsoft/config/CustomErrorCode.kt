package com.assignment.greencatsoft.config

import org.apache.http.HttpStatus


interface ErrorCode {
    val code: String
    val label: String
    val statusCode: Int
}

object CustomErrorCode {

    object BodyError : ErrorCode {
        override val code = "BODY_ERROR"
        override val label = "데이터를 올바르게 입력해주세요"
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object PasswordDifferent : ErrorCode {
        override val code = "PASSWORD_DIFFERENT"
        override val label: String = "비밀번호가 일치하지 않습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object LoginFailed : ErrorCode {
        override val code = "LOGIN_FAILED"
        override val label = "로그인에 실패하였습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object EmailRegex : ErrorCode {
        override val code = "EMAIL_REGEX"
        override val label = "이메일 형식을 확인해주세요."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object PasswordRegex : ErrorCode {
        override val code = "PASSWORD_REGEX"
        override val label = "비밀번호 형식을 확인해주세요"
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }
}
