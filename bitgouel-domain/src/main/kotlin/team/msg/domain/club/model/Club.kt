package team.msg.domain.club.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.enums.Field
import team.msg.domain.school.model.School

@Entity
class Club(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "industry", columnDefinition = "VARCHAR(100)", nullable = false)
    val field: Field

)