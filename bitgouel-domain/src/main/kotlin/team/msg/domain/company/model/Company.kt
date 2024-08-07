package team.msg.domain.company.model

import team.msg.common.enums.Field
import javax.persistence.*

@Entity
class Company(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "name", nullable = false, unique = true)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "field", columnDefinition = "VARCHAR(100)", nullable = false)
    val field: Field

)