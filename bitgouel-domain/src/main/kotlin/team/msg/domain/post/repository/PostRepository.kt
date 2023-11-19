package team.msg.domain.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.post.model.Post
import java.util.UUID

interface PostRepository : JpaRepository<Post, UUID>