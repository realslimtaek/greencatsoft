package com.assignment.greencatsoft.adaptor.`in`.group.user

import com.assignment.greencatsoft.application.port.`in`.groupUser.GroupUserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.token.TokenQueryUseCase
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}

data class GroupInviteReqDto(
    @field:Schema(name = "groupId", example = "1", description = "초대할 그룹의 ID", nullable = false)
    override val groupId: Long,
    @field:Schema(name = "owner", example = "", description = "token에서 자동으로 파싱됩니다.", nullable = false)
    override var owner: String = "",
    @field:Schema(name = "email", example = "asdf@asdf.com", description = "초대할 대상의 이메일. 해당 이메일은 회원이어야합니다.", nullable = false)
    override val email: String,
) : GroupInviteReq
