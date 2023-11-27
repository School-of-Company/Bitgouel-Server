package team.msg.domain.post.repository.custom

import java.util.UUID

interface CustomPostRepository {
    fun deleteAllByUserId(userId: UUID)
}