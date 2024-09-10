package team.msg.domain.post.model

import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.post.enums.FeedType
import java.util.*
import javax.persistence.*

@Entity
class Post (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    val userId: UUID,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    var title: String,

    @Column(columnDefinition = "VARCHAR(5000)", nullable = false)
    var content: String,

    @Generated(GenerationTime.INSERT)
    @Column(columnDefinition = "INT NOT NULL UNIQUE KEY auto_increment", nullable = false, insertable = false)
    val postSequence: Int = 0,

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