package com.assignment.greencatsoft.application.service.user

import com.assignment.greencatsoft.adaptor.`in`.user.LoginRes
import com.assignment.greencatsoft.adaptor.`in`.user.UpdateUserInfoReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserLoginReq
import com.assignment.greencatsoft.adaptor.`in`.user.UserSignInReq
import com.assignment.greencatsoft.adaptor.out.user.UserEntity
import com.assignment.greencatsoft.application.port.`in`.token.TokenOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserOperationUseCase
import com.assignment.greencatsoft.application.port.`in`.user.UserQueryUseCase
import com.assignment.greencatsoft.application.port.out.group.GroupSavePort
import com.assignment.greencatsoft.application.port.out.groupUser.GroupUserSavePort
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
    private val groupSavePort: GroupSavePort,
    private val passwordEncoder: PasswordEncoder,
    private val tokenOperationUseCase: TokenOperationUseCase,
    private val responseMapper: UserResponseMapper,
    private val groupUserSavePort: GroupUserSavePort,
) : UserQueryUseCase, UserOperationUseCase {

    override fun signIn(req: UserSignInReq) {
        userGetPort.checkExistsEmail(req.email)
        val user = userSavePort.save(req)

        groupSavePort.makePersonalGroup(user)
            .also { groupUserSavePort.makeOwnGroupUser(it.id!!, user.email) }
    }

    override fun updateInfo(req: UpdateUserInfoReq) = userGetPort.findByEmail(req.email)
        .apply {
            this.name = req.name
            this.status = UserEntity.UserStatus.ACTIVE
        }
        .run(userSavePort::save)
        .run(responseMapper::toUpdateRes)

    override fun login(req: UserLoginReq): LoginRes {
        val user = userGetPort.findByEmail(req.email)
        if (!passwordEncoder.matches(req.password, user.password)) {
            throwError(CustomErrorCode.LoginFailed)
        }

        val (token, cookie) = tokenOperationUseCase.createToken(user)

        return responseMapper.toLoginRes(token, cookie, user)
    }
}
