package team.msg.domain.admin.service

import org.springframework.web.multipart.MultipartFile
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.presentation.data.response.UsersResponse
import java.util.*

interface AdminService {
    fun queryUsers(request: QueryUsersRequest): UsersResponse
    fun approveUsers(userIds: List<UUID>)
    fun rejectUsers(userIds: List<UUID>)
    fun forceWithdraw(userIds: List<UUID>)
    fun uploadStudentListExcel(file: MultipartFile)
}