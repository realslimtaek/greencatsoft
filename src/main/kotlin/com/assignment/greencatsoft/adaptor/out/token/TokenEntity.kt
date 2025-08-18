package com.assignment.greencatsoft.adaptor.out.token

import com.assignment.greencatsoft.adaptor.out.BaseEntity
import com.assignment.greencatsoft.config.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "TOKEN")
class TokenEntity(
    @Column(name = "EMAIL", nullable = false, columnDefinition = "varchar(30)")
    val email: String,

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "REFRESH_TOKEN", columnDefinition = "text", nullable = false)
    var refreshToken: String,

    @Column(name = "REFRESH_EXPIRED_AT", columnDefinition = "datetime", nullable = false)
    var refreshExpireAt: LocalDateTime,

) : BaseEntity()
