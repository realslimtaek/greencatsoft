package com.assignment.greencatsoft.adaptor.out.user

import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdaptor(
    private val userRepository: UserRepository,
    private val userMapper: UsersMapper,
) : UserGetPort, UserSavePort
