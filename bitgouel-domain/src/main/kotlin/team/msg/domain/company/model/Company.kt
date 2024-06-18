package team.msg.domain.company.model

import team.msg.common.enums.Industry
import javax.persistence.*

@Entity
class Company(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = false)
    val id: Long = 0,

    @Column(name = "name")
    val name: String,

    @Column(name = "industry", columnDefinition = "VARCHAR(100)", nullable = false)
    val industry: Industry

)