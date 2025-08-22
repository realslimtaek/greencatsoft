package com.assignment.greencatsoft.adaptor.`in`.user

import com.assignment.greencatsoft.application.port.`in`.token.TokenQueryUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryUseCase
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@Tag(name = "사용자", description = "사용자 관련 API")
class UserEndpoint(
    private val userQueryUseCase: UserQueryUseCase,
    private val userOperationUseCase: UserOperationUseCase,
    private val tokenQueryUseCase: TokenQueryUseCase,
) {

    @PostMapping("/signin")
    @Operation(
        summary = "사용자 회원가입",
        description = "회원가입을 진행합니다. 서비스를 이용하려면 로그인이 필요합니다." +
            "<br>회원가입이 완료되면, 개인 그룹이 생성됩니다.",
    )
    fun signIn(@RequestBody req: UserSignInReqDto): ResponseEntity<String> {
        userOperationUseCase.signIn(req)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping
    @Operation(
        summary = "사용자 정보 수정",
        description = "사용자 정보를 수정합니다." +
            "<br>회원가입을 하면 회원 이름이 들어가있지 않으므로 회원가입을 한 이후 해당 API를 호출해야합니다." +
            "<br>해당 API를 호출 한 뒤, 프론트엔드는 token을 재발급 받아야합니다.",
    )
    @SecurityRequirement(name = "Authorization")
    fun updateInfo(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @RequestBody req: UpdateUserInfoReqDto,
    ): ResponseEntity<UserInfoRes> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        val res = userOperationUseCase.updateInfo(req.apply { this.email = email })
        return ResponseEntity.ok(res)
    }

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        description = "로그인을 진행합니다." +
            "<br>Status가 PENDING이라면, 사용자 정보를 업데이트를 해야합니다.",
    )
    fun login(@RequestBody req: UserLoginReqDto): ResponseEntity<UserInfoRes> {
        val res = userQueryUseCase.login(req)

        return ResponseEntity.ok()
            .header("Authorization", res.token)
            .header("Set-Cookie", res.cookie)
            .body(res.res)
    }
}

data class UpdateUserInfoReqDto(
    @field:Schema(name = "name", example = "홍길동", description = "사용자 이름", nullable = false)
    override val name: String,
) : UpdateUserInfoReq {
    @field:Schema(hidden = true)
    override lateinit var email: String
}

data class UserLoginReqDto(
    @field:Schema(name = "email", example = "asdf@asdf.com", description = "사용자 이메일", nullable = false)
    override val email: String,
    @field:Schema(name = "password", example = "Qwer1234!", description = "사용자 비밀번호", nullable = false)
    override val password: String,
) : UserLoginReq

data class UserSignInReqDto(
    @field:Schema(name = "email", example = "asdf@asdf.com", description = "사용자 이메일을 입력합니다. 이메일 형식이 지켜져야합니다.", nullable = false)
    override val email: String,
    @field:Schema(name = "password", example = "Qwer1234!", description = "대문자, 소문자, 특수문자, 숫자를 모두 포함해야합니다.", nullable = false)
    override val password: String,
    @field:Schema(name = "rePassword", example = "Qwer1234!", description = "비밀번호를 재입력해야합니다.", nullable = false)
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
