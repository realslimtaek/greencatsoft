package com.assignment.greencatsoft.adaptor.`in`.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class UserEndpoint {

    @GetMapping()
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("Success")
    }


}
