package team.msg.domain.school.model

import javax.persistence.*
import team.msg.common.enums.Field
import team.msg.common.enums.Line

@Entity
class School(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "logoImageUrl", columnDefinition = "VARCHAR(100)", nullable = false)
    val logoImageUrl: String,

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false, unique = true, updatable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "field", columnDefinition = "VARCHAR(50)", nullable = false)
    val field: Field,

    @Enumerated(EnumType.STRING)
    @Column(name = "line", columnDefinition = "VARCHAR(50)", nullable = false)
    val line: Line,

    @ElementCollection
    @CollectionTable(
        name = "department",
        joinColumns = [JoinColumn(name = "school_id")]
    )
    val departments: List<String>

)