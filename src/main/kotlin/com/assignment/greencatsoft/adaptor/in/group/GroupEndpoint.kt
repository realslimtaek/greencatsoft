package com.assignment.greencatsoft.adaptor.`in`.group

import com.assignment.greencatsoft.application.port.`in`.group.GroupOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.group.GroupQueryUseCase
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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/group")
@Tag(name = "그룹 설정", description = "그룹 관련 API")
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.APIKEY, `in` = SecuritySchemeIn.HEADER)
@SecurityRequirement(name = "Authorization")
class GroupEndpoint(
    private val queryUseCase: GroupQueryUseCase,
    private val operationUseCase: GroupOperationUseCase,
    private val tokenQueryUseCase: TokenQueryUseCase,
) {

    @PostMapping
    @Operation(summary = "그룹 생성", description = "다른 사용자와 일정을 공유할 수 있는 그룹을 생성합니다.")
    fun addGroup(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @RequestBody req: GroupAddReqDto,
    ): ResponseEntity<DefaultGroupRes> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)
        val res = operationUseCase.addGroup(req.apply { this.owner = email })

        return ResponseEntity.ok(res)
    }

    @GetMapping
    @Operation(summary = "그룹 조회", description = "모든 그룹을 조회합니다.")
    fun getGroups(@Parameter(hidden = true) @RequestHeader("Authorization") token: String): ResponseEntity<List<GroupListRes>> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)
        val res = queryUseCase.getGroups(email)

        return ResponseEntity.ok(res)
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "그룹을 탈퇴합니다.", description = "가입한 그룹을 탈퇴합니다. 내가 그룹 소유자 일경우, 해당 그룹을 삭제합니다.")
    fun deleteGroup(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @PathVariable groupId: Long,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        operationUseCase.deleteGroup(email, groupId)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}

data class GroupAddReqDto(
    @field:Schema(name = "name", example = "가족여행", description = "그룹명", nullable = false)
    override val name: String,
) : GroupAddReq {
    override lateinit var owner: String
}
