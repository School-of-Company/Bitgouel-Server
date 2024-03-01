package team.msg.domain.user.repository.custom

import team.msg.common.enums.ApproveStatus
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.custom.projection.UserNameProjectionData
import java.util.*

interface CustomUserRepository {
    fun query(keyword: String, authority: Authority, approveStatus: ApproveStatus): List<User>
    fun queryNameById(id: UUID): UserNameProjectionData?
    fun queryInstructorsAndOrganization(keyword: String): List<Pair<User, String>>
}