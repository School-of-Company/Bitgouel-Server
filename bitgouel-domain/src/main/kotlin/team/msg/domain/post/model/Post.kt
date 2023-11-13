package team.msg.domain.post.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.post.enums.FeedType
import java.util.*

@Entity
class Post (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val feedType: FeedType
) : BaseUUIDEntity(id)