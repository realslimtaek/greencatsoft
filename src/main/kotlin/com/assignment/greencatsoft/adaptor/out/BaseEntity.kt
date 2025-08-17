package com.assignment.greencatsoft.adaptor.out

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@MappedSuperclass
abstract class BaseEntity(
    @Id @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "CREATED_AT", columnDefinition = "DATETIME", updatable = false)
    @CreatedDate
    val createdAt: LocalDateTime = current,
    @Column(name = "UPDATED_AT", columnDefinition = "DATETIME")
    @UpdateTimestamp
    var updatedAt: LocalDateTime = current,
)

private val current get() = LocalDateTime.now(ZoneOffset.of("Asia/Seoul"))
