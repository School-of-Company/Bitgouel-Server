package team.msg.domain.user.model

import team.msg.common.entity.BaseUUIDEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.enums.SignUpStatus
import java.util.*

@Entity
class User(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    val email: String,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    val phoneNumber: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val authority: Authority,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val signUpStatus: SignUpStatus

) : BaseUUIDEntity(id)