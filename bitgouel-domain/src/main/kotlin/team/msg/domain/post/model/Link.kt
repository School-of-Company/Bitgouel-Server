package team.msg.domain.post.model

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import team.msg.common.entity.BaseUUIDEntity
import java.util.*

@Entity
class Link (
    @get:JvmName("getIdentifier")
    override var id: UUID,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "post_id", columnDefinition = "BINARY(16)")
    val post: Post,

    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String
) : BaseUUIDEntity(id)