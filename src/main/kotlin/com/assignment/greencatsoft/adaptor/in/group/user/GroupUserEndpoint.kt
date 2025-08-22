package com.assignment.greencatsoft.adaptor.`in`.group.user

import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.token.TokenQueryUseCase
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/group/user")
@Tag(name = "그룹 설정", description = "그룹 관련 API")
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.APIKEY, `in` = SecuritySchemeIn.HEADER)
@SecurityRequirement(name = "Authorization")
class GroupUserEndpoint(
    private val tokenQueryUseCase: TokenQueryUseCase,
    private val groupUserOperationUseCase: GroupUserOperationUseCase,
) {

    @PostMapping("/invite")
    @Operation(summary = "그룹 초대", description = "다른 사용자를 그룹에 초대합니다.")
    fun inviteGroup(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @RequestBody req: GroupInviteReqDto,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)
        groupUserOperationUseCase.inviteGroup(req.apply { this.owner = email })

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/accept/{groupId}")
    @Operation(summary = "그룹 초대 수락", description = "초대받은 그룹 요청을 수락합니다.")
    @Parameter(name = "groupId", description = "그룹ID", example = "1", required = true)
    fun acceptGroup(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @PathVariable groupId: Long,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        groupUserOperationUseCase.acceptGroupInvite(email, groupId)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @DeleteMapping("/decline/{groupId}")
    @Operation(summary = "그룹 초대 거부", description = "초대받은 그룹 요청을 거부합니다.")
    fun declineGroup(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @PathVariable groupId: Long,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        groupUserOperationUseCase.declineGroupInvite(email, groupId)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}

data class GroupInviteReqDto(
    @field:Schema(name = "groupId", example = "1", description = "초대할 그룹의 ID", nullable = false)
    override val groupId: Long,
    @field:Schema(name = "email", example = "asdf@asdf.com", description = "초대할 대상의 이메일. 해당 이메일은 회원이어야합니다.", nullable = false)
    override val email: String,
) : GroupInviteReq {
    override lateinit var owner: String


    init {
        validate()
    }

    override fun validate() {
        if (!emailRegex.matches(email)) {
            throwError(CustomErrorCode.EmailRegex)
        }
    }

    companion object {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    }
}
