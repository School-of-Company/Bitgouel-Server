package team.msg.domain.university.model

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn

@Entity
class University(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "name", unique = true, nullable = false)
    val name: String,

    @ElementCollection
    @CollectionTable(name = "department", joinColumns = [JoinColumn(name = "university_id")])
    val department: List<String>
)