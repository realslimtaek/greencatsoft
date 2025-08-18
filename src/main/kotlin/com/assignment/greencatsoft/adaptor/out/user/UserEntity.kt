package com.assignment.greencatsoft.adaptor.out.user

import com.assignment.greencatsoft.adaptor.out.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "USER")
class UserEntity(

    @Column(name = "EMAIL", nullable = false, unique = true, columnDefinition = "varchar(30)")
    @Comment("이메일")
    val email: String,

    @Column(name = "PASSWORD", nullable = false, columnDefinition = "varchar(100)")
    @Comment("비밀번호")
    var password: String,

    @Column(name = "NAME", columnDefinition = "varchar(10)")
    @Comment("사용자 명")
    var name: String? = null,

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("회원 상태 (추가 정보 미 입력시 = Pending, 탈퇴 시 = Resign, 정상 = ACTIVE)")
    var status: UsersStatus = UsersStatus.PENDING,

) : BaseEntity() {

    enum class UsersStatus() {
        PENDING,
        ACTIVE,
        RESIGN,
    }
}
