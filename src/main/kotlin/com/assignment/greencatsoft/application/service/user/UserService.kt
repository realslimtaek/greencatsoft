package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserLoginReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq
import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryUseCase
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import com.assignment.greencatsoft.config.CustomErrorCode
import com.assignment.greencatsoft.config.throwError
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Transactional
class UserService(
    private val userSavePort: UserSavePort,
    private val userGetPort: UserGetPort,
    private val passwordEncoder: PasswordEncoder,
    private val tokenOperationUseCase: TokenOperationUseCase,
) : UserQueryUseCase, UserOperationUseCase {

    override fun signIn(req: UserSignInReq) {
        userGetPort.checkExistsEmail(req.email)
        userSavePort.save(req)
    }

    override fun login(req: UserLoginReq): Pair<String, String> {
        val user = userGetPort.findByEmail(req.email)
        if (!passwordEncoder.matches(req.password, user.password)) {
            throwError(CustomErrorCode.LoginFailed)
        }

        return tokenOperationUseCase.createToken(user)
    }
}
