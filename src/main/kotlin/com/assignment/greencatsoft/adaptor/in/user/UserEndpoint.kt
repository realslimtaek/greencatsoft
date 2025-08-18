package com.assignment.greencatsoft.adaptor.`in`.user

import com.assignment.greencatsoft.application.port.`in`.user.UserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryUseCase
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserEndpoint(
    private val userQueryUseCase: UserQueryUseCase,
    private val userOperationUseCase: UserOperationUseCase,
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody req: UserSignInReqDto): ResponseEntity<String> {
        userOperationUseCase.signIn(req)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody req: UserLoginReqDto): ResponseEntity<String> {
        val res = userQueryUseCase.login(req)

        return ResponseEntity.ok()
            .header("Authorization", res.first)
            .header("Set-Cookie", res.second)
            .body("Success")
    }
}

data class UserLoginReqDto(
    override val email: String,
    override val password: String,
) : UserLoginReq

data class UserSignInReqDto(
    override val email: String,
    override val password: String,
    override val rePassword: String,
) : UserSignInReq {

    init {
        validate()
    }

    override fun validate() {
        if (!emailRegex.matches(email)) {
            throwError(CustomErrorCode.EmailRegex)
        }

        if (!passwordRegex.matches(password)) {
            throwError(CustomErrorCode.PasswordRegex)
        }

        if (password != rePassword) {
            throwError(CustomErrorCode.PasswordDifferent)
        }
    }

    companion object {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val passwordRegex = Regex(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{}|;':\",.<>?/]).{8,}\$",
        )
    }
}
