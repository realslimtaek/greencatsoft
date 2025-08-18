package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq
import com.assignment.greencatsoft.application.port.`in`.user.UserOperationPort
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryPort
import com.assignment.greencatsoft.application.port.out.users.UserGetPort
import com.assignment.greencatsoft.application.port.out.users.UserSavePort
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class UserService(
    private val userSavePort: UserSavePort,
    private val userGetPort: UserGetPort,
) : UserQueryPort, UserOperationPort {

    override fun signIn(req: UserSignInReq) {
        userGetPort.checkExistsEmail(req.email)
        userSavePort.save(req)
    }
}
