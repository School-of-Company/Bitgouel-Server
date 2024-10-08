package team.msg.domain.admin.service

import org.springframework.web.multipart.MultipartFile
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.user.presentation.data.response.UsersResponse
import java.util.*
import javax.servlet.http.HttpServletResponse

interface AdminService {
    fun queryUsers(request: QueryUsersRequest): UsersResponse
    fun approveUsers(userIds: List<UUID>)
    fun rejectUsers(userIds: List<UUID>)
    fun forceWithdraw(userIds: List<UUID>)
    fun uploadStudentListExcel(file: MultipartFile)
    fun uploadClubListExcel(file: MultipartFile)
    fun downloadClubStatusExcel(response: HttpServletResponse)
    fun uploadLectureListExcel(file: MultipartFile)
}