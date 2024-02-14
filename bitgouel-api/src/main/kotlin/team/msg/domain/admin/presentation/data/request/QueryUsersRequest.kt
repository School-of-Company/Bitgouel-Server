package team.msg.domain.admin.presentation.data.request

import team.msg.common.enums.ApproveStatus
import team.msg.domain.user.enums.Authority

data class QueryUsersRequest(
    val keyword: String,
    val authority: Authority,
    val approveStatus: ApproveStatus
)