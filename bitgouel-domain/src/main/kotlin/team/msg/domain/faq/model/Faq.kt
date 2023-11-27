package team.msg.domain.faq.model

import team.msg.domain.admin.model.Admin
import javax.persistence.*

@Entity
class Faq(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, columnDefinition = "TEXT")
    val question: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", columnDefinition = "BINARY(16)")
    val admin: Admin

)