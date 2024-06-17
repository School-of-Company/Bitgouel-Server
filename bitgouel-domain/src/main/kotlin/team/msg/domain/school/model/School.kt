package team.msg.domain.school.model

import javax.persistence.*

@Entity
class School(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = false)
    val id: Long = 0,

    @Column(name = "highSchool", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    val highSchool: String

)