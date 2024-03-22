package team.msg.domain.post.model

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.post.enums.FeedType
import java.util.*

@Entity
class Post (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Generated(GenerationTime.INSERT)
    @Column(columnDefinition = "INT", nullable = false, insertable = false)
    val postSequence: Long,

    @ElementCollection
    @CollectionTable(
        name = "Link",
        joinColumns = [JoinColumn(name = "post_id")]
    )
    val links: List<String>,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val feedType: FeedType
) : BaseUUIDEntity(id)