package com.assignment.greencatsoft.adaptor.out.groupUser

import com.assignment.greencatsoft.adaptor.out.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "GROUP_USER")
class GroupUserEntity(

    @Column(name = "GROUP_ID", nullable = false, columnDefinition = "bigint(11)")
    @Comment("그룹아이디")
    val groupId: Long,

    @Column(name = "USER_ID", nullable = false, columnDefinition = "bigint(11)")
    @Comment("사용자아이디")
    val userId: Long,

    @Column(name = "ACCEPTED", nullable = false, columnDefinition = "tinyint(1)")
    @Comment("그룹 초대 수락 여부")
    var accepted: Boolean = false,

) : BaseEntity()
