package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.out.user.UserRepository
import com.assignment.greencatsoft.application.port.`in`.user.UserOperationPort
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) : UserQueryPort, UserOperationPort
