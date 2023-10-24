package team.msg.domain.user.event

import team.msg.domain.user.model.User

/**
 * User 가 withdraw 하는 이벤트를 나타냅니다.
 * User withdraw 를 구독자에게 알리기 위해서 사용됩니다.
 */
data class WithdrawUserEvent(
    val user: User
)