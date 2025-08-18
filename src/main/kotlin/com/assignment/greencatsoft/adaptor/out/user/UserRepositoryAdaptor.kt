package com.assignment.greencatsoft.adaptor.out.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import com.assignment.greencatsoft.domain.user.User
import org.springframework.stereotype.Component

@Component
class UserRepositoryAdaptor(
    private val userRepository: UserRepository,
    private val userMapper: UsersMapper,
) : UserGetPort, UserSavePort {

    override fun checkExistsEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throwError(CustomErrorCode.DuplicateEmail)
        }
    }

    override fun save(req: UserSignInReq) {
        userRepository.save(
            userMapper.toEntity(req),
        )
    }

    override fun findByEmail(email: String): User = userRepository.findByEmail(email)
        ?.run(userMapper::toDomain)
        ?: throwError(CustomErrorCode.NotFoundUser)
}
