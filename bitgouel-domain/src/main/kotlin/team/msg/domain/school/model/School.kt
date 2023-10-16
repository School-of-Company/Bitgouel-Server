package team.msg.domain.school.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import team.msg.domain.school.enums.HighSchool
import java.util.UUID

@Entity
class School(

    override val id: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "highSchool", columnDefinition = "VARCHAR(30)", nullable = false, unique = true)
    val highSchool: HighSchool // 학교 이름 상수

) : team.msg.common.entity.BaseUUIDEntity(id)