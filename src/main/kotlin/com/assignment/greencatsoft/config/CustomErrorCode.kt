package com.assignment.greencatsoft.config

import org.apache.http.HttpStatus

interface ErrorCode {
    val code: String
    val label: String
    val statusCode: Int
}

object CustomErrorCode {

    object InvalidToken : ErrorCode {
        override val code = "INVALID_TOKEN"
        override val label = "유효하지 않은 토큰입니다."
        override val statusCode = HttpStatus.SC_UNAUTHORIZED
    }

    object TokenExpired : ErrorCode {
        override val code = "TOKEN_EXPIRED"
        override val label = "토큰이 만료되었습니다."
        override val statusCode = HttpStatus.SC_UNAUTHORIZED
    }

    object NotFoundInvite : ErrorCode {
        override val code = "NOT_FOUND_INVITE"
        override val label = "초대내역을 찾을 수 없습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object NotFoundUser : ErrorCode {
        override val code = "NOT_FOUND_USER"
        override val label = "사용자를 찾을 수 없습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object NotFoundGroup : ErrorCode {
        override val code = "NOT_FOUND_GROUP"
        override val label = "그룹을 찾을 수 없습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object NotGroupOwner : ErrorCode {
        override val code = "NOT_GROUP_OWNER"
        override val label = "해당 그룹장이 아닙니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object UserAlreadyInvited : ErrorCode {
        override val code = "USER_ALREADY_INVITED"
        override val label = "이미 초대된 사용자입니다."
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

    object DuplicateEmail : ErrorCode {
        override val code = "DUPLICATE_EMAIL"
        override val label = "해당 이메일은 가입 이력이 있습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object DataNotFound : ErrorCode {
        override val code = "DATA_NOT_FOUND"
        override val label = "데이터를 찾을 수 없습니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object NotMySchedule : ErrorCode {
        override val code = "NOT_MY_SCHEDULE"
        override val label = "사용자의 일정이 아닙니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object NotMyGroup : ErrorCode {
        override val code = "NOT_MY_GROUP"
        override val label = "해당 그룹의 일원이 아닙니다."
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }

    object DataError : ErrorCode {
        override val code = "DATA_ERROR"
        override val label = "데이터 입력 오류"
        override val statusCode = HttpStatus.SC_BAD_REQUEST
    }
}
