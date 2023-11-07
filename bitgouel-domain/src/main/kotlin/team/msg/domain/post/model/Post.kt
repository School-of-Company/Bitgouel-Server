package team.msg.domain.post.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.post.enums.FeedType
import team.msg.domain.user.model.User
import java.util.*

@Entity
class Post (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: User,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    var title: String,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    var content: String,

    @Column(columnDefinition = "VARCHAR(2083)", nullable = true)
    var link: String?,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val feedType: FeedType
) : BaseUUIDEntity(id)