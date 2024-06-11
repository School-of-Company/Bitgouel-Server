package team.msg.domain.school.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import team.msg.domain.school.enums.HighSchool

@Entity
class School(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = false)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "highSchool", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    val highSchool: HighSchool
)