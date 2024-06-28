package team.msg.domain.club.model

import team.msg.common.enums.Field
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.domain.school.model.School

@Entity
class Club(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "school_id", nullable = false)
    val school: School,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(name = "industry", columnDefinition = "VARCHAR(100)", nullable = false)
    val field: Field

)