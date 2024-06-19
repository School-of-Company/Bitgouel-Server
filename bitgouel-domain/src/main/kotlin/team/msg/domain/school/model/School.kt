package team.msg.domain.school.model

import javax.persistence.*

@Entity
class School(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

<<<<<<< HEAD
    @Enumerated(EnumType.STRING)
    @Column(name = "highSchool", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    val highSchool: HighSchool,

    @Column(name = "logoImageUrl", columnDefinition = "VARCHAR(100)", nullable = false)
    val logoImageUrl: String
=======
    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false, unique = true, updatable = false)
    val name: String
>>>>>>> 740a8121de6b045796d00171fa98a66367a849bf

)