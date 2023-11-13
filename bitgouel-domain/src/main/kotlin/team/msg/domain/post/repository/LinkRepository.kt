package team.msg.domain.post.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.post.model.Link
import team.msg.domain.post.model.Post
import java.util.*

interface LinkRepository : JpaRepository<Link,UUID> {
    @EntityGraph(attributePaths = ["post"])
    fun findByPost(post: Post): List<Link>
}