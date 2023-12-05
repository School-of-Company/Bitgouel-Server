package team.msg.domain.user.repository.custom.projection

import com.querydsl.core.annotations.QueryProjection

data class UserNameProjectionData @QueryProjection constructor (
    val name: String
)
