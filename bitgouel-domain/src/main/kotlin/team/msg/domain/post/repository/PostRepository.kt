package team.msg.domain.post.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.post.enums.FeedType
import team.msg.domain.post.model.Post
import java.util.UUID

interface PostRepository : JpaRepository<Post, UUID> {
    fun findAllByFeedType(feedType: FeedType, pageable: Pageable): Page<Post>
}