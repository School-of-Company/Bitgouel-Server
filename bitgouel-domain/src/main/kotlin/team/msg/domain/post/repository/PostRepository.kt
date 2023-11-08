package team.msg.domain.post.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.post.model.Post
import java.util.UUID

interface PostRepository : CrudRepository<Post, UUID>