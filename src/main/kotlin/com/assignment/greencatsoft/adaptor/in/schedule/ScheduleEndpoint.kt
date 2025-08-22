package com.assignment.greencatsoft.adaptor.`in`.schedule

import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.schedule.ScheduleQueryUseCase
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@RestController
@RequestMapping("/schedule")
@Tag(name = "일정 관리", description = "일정 설정 관련 API")
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.APIKEY, `in` = SecuritySchemeIn.HEADER)
@SecurityRequirement(name = "Authorization")
class ScheduleEndpoint(
    private val queryUseCase: ScheduleQueryUseCase,
    private val operationUseCase: ScheduleOperationUseCase,
    private val tokenQueryUseCase: TokenQueryUseCase,
) {

    @PostMapping
    @Operation(summary = "일정 등록", description = "일정을 등록합니다.")
    fun addSchedule(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @RequestBody req: AddScheduleReqDto,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        operationUseCase.addSchedule(req.apply { this.writer = email })
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping
    @Operation(summary = "일정 수정", description = "일정을 수정합니다.")
    fun updateSchedule(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @RequestBody req: UpdateScheduleReqDto,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)
        operationUseCase.updateSchedule(req.apply { this.writer = email })
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @GetMapping
    @Operation(summary = "일정 조회", description = "일정을 조회합니다.")
    @Parameter(name = "yearMonth", description = "검색 기준 연-월", example = "2025-08", required = true)
    @Parameter(name = "groupId", description = "그룹 id, 특정 그룹의 일정만 조회할때 사용합니다.", example = "1", required = false)
    fun getSchedule(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        yearMonth: YearMonth,
        groupId: Long?,
    ): ResponseEntity<List<GetScheduleRes>> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        val res = queryUseCase.getSchedule(email, yearMonth, groupId)
        return ResponseEntity.ok(res)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    @Parameter(name = "id", description = "일정 id", example = "1", required = true)
    fun deleteSchedule(
        @Parameter(hidden = true) @RequestHeader("Authorization") token: String,
        @PathVariable id: Long,
    ): ResponseEntity<String> {
        val (email, _) = tokenQueryUseCase.getSubAndRole(token)

        operationUseCase.deleteSchedule(email, id)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}

data class AddScheduleReqDto(
    @field:Schema(name = "groupId", example = "1", description = "해당 일정이 소속될 그룹을 정합니다.", required = true)
    override val groupId: Long,
    @field:Schema(name = "title", example = "점심시간", description = "일정 제목", required = true)
    override val title: String?,
    @field:Schema(name = "startDate", example = "2025-08-01", description = "일정 시작 일자", required = true)
    override val startDate: LocalDate,
    @field:Schema(name = "startTime", example = "12:00:00", description = "일정 시작 시각, null 이면 종일 일정입니다.", required = true)
    override val startTime: LocalTime?,
    @field:Schema(name = "endDate", example = "2025-08-01", description = "일정 종료 일자", required = true)
    override val endDate: LocalDate,
    @field:Schema(name = "endTime", example = "14:00:00", description = "일정 종료 시각, null 이면 종일 일정입니다.", required = true)
    override val endTime: LocalTime?,
    @field:Schema(name = "memo", example = "제육볶음먹자", description = "일정 메모", required = true)
    override val memo: String?,
) : AddScheduleReq {
    @field:Schema(hidden = true)
    override lateinit var writer: String
}

data class UpdateScheduleReqDto(
    @field:Schema(name = "id", example = "1", description = "일정", required = true)
    override val id: Long,
    @field:Schema(name = "groupId", example = "1", description = "해당 일정이 소속될 그룹을 정합니다.", required = true)
    override val groupId: Long,
    @field:Schema(name = "title", example = "나의 일정입니다", description = "일정 제목", required = true)
    override val title: String?,
    @field:Schema(name = "startDate", example = "2025-01-01", description = "일정 시작 일자", required = true)
    override val startDate: LocalDate,
    @field:Schema(name = "startTime", example = "1", description = "일정 시작 시각, null 이면 종일 일정입니다.", required = true)
    override val startTime: LocalTime?,
    @field:Schema(name = "endDate", example = "1", description = "일정 종료 일자", required = true)
    override val endDate: LocalDate,
    @field:Schema(name = "endTime", example = "1", description = "일정 종료 시각, null 이면 종일 일정입니다.", required = true)
    override val endTime: LocalTime?,
    @field:Schema(name = "memo", example = "1", description = "일정 메모", required = true)
    override val memo: String?,
) : UpdateScheduleReq {
    @field:Schema(hidden = true)
    override lateinit var writer: String

    init {
        if ((startTime == null && endTime != null) || (startTime != null && endTime == null)) {
            throwError(CustomErrorCode.DataError)
        }
    }
}
