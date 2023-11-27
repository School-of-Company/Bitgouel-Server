package team.msg.domain.post.presentation.data.response

import org.springframework.data.domain.Page
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.model.Post
import java.time.LocalDateTime
import java.util.UUID

data class PostResponse (
    val id: UUID,
    val title: String,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun of(post: Post) =
            PostResponse(
                id = post.id,
                title = post.title,
                modifiedAt = post.modifiedAt,
            )

        fun pageOf(posts: Page<Post>) =
            PostsResponse(
                posts.map {
                    of(it)
                }
            )

        fun detailOf(post: Post, writer: String) =
            PostDetailsResponse(
                title = post.title,
                writer = writer,
                content = post.content,
                feedType = post.feedType,
                modifiedAt = post.modifiedAt,
                links = post.link
            )
    }
}

data class PostsResponse(
    val posts: Page<PostResponse>
)

data class PostDetailsResponse(
    val title: String,
    val writer: String,
    val content: String,
    val feedType: FeedType,
    val modifiedAt: LocalDateTime,
    val links: List<String>
)