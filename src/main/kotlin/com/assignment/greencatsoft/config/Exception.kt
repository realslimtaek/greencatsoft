package com.assignment.greencatsoft.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class EndpointExceptionHandler {

    @ExceptionHandler(ApplicationException::class)
    fun handleResponse(t: ApplicationException): ResponseEntity<MessageResponse> = t.response(t.classify.statusCode)

    private fun ApplicationException.response(status: Int): ResponseEntity<MessageResponse> = ResponseEntity.status(
        status,
    ).body(MessageResponse(this.classify.code, this.message, this.classify.innerCode))

    data class MessageResponse(
        val classify: String,
        val message: String?,
        val innerCode: Int,
    )
}

class ApplicationException(
    val classify: ErrorCode,
    override val cause: Throwable? = null,
) : RuntimeException(classify.label) {

    override val message: String get() = classify.label
}

fun throwError(error: ErrorCode, t: Throwable? = null): Nothing {
    throw ApplicationException(error, t)
}
