package com.assignment.greencatsoft.adaptor.out.group

import com.assignment.greencatsoft.adaptor.out.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment

@Entity
@Table(name = "GROUP")
class GroupEntity(

    @Column(name = "NAME", columnDefinition = "varchar(10)", nullable = false)
    @Comment("그룹명")
    var name: String,

    @Column(name = "PRIVATE", columnDefinition = "tinyint(1)", nullable = false)
    @Comment("비공개 여부, true = 공개 그룹, false = 비공개 그룹")
    val private: Boolean,

) : BaseEntity()
