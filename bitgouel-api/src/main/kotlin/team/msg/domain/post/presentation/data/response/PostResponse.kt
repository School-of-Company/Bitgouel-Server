package team.msg.domain.post.presentation.data.response

import org.springframework.data.domain.Page
import team.msg.domain.post.model.Post
import java.time.LocalDateTime
import java.util.UUID

data class PostResponse (
    val id: UUID,
    val title: String,
    val modifiedAt: LocalDateTime,
){

    fun pageOf(posts: Page<Post>) =
        PostsResponse(
            posts.map {
                PostResponse(
                    id = it.id,
                    title = it.title,
                    modifiedAt = it.modifiedAt,
                )
            }
        )
}

data class PostsResponse(
    val posts: Page<PostResponse>
)