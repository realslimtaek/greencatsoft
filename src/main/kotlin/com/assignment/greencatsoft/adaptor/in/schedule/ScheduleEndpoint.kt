package com.assignment.greencatsoft.adaptor.`in`.schedule

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/schedule")
@Tag(name = "일정 관리", description = "일정 설정 관련 API")
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.APIKEY, `in` = SecuritySchemeIn.HEADER)
@SecurityRequirement(name = "Authorization")
class ScheduleEndpoint(
)
