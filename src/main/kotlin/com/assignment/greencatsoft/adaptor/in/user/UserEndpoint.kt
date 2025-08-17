package com.assignment.greencatsoft.adaptor.`in`.user

import com.assignment.greencatsoft.application.port.`in`.user.UserOperationPort
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserEndpoint(
    private val userQueryPort: UserQueryPort,
    private val userOperationPort: UserOperationPort,
) {

    @GetMapping()
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("Success")
    }
}
