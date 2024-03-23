package team.msg.domain.post.repository.custom.projection

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime
import java.util.UUID

data class PostProjection @QueryProjection constructor (
    val id: UUID,
    val title: String,
    val modifiedAt: LocalDateTime,
    val postSequence: Int
)