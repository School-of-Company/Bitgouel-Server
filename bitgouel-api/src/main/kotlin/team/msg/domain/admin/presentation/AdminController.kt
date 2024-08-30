package team.msg.domain.admin.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import team.msg.domain.admin.mapper.AdminMapper
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest
import team.msg.domain.admin.service.AdminService
import team.msg.domain.user.presentation.data.response.UsersResponse
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val adminMapper: AdminMapper
) {
    @GetMapping
    fun queryUsers(webRequest: QueryUsersWebRequest): ResponseEntity<UsersResponse> {
        val request = adminMapper.queryUsersWebRequestToDto(webRequest)
        val response = adminService.queryUsers(request)
        return ResponseEntity.ok(response)
    }

    @PatchMapping
    fun approveUsers(@RequestParam userIds: List<UUID>): ResponseEntity<Unit> {
        adminService.approveUsers(userIds)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/reject")
    fun rejectUsers(@RequestParam userIds: List<UUID>): ResponseEntity<Unit> {
        adminService.rejectUsers(userIds)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/withdraw")
    fun forceWithdraw(@RequestParam userIds: List<UUID>): ResponseEntity<Unit> {
        adminService.forceWithdraw(userIds)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/student/excel")
    fun uploadStudentListExcel(@RequestPart file: MultipartFile): ResponseEntity<Unit> {
        adminService.uploadStudentListExcel(file)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/club/excel")
    fun uploadClubListExcel(@RequestPart file: MultipartFile): ResponseEntity<Unit> {
        adminService.uploadClubListExcel(file)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/club/excel")
    fun clubStatusExcel(response: HttpServletResponse): ResponseEntity<Unit> {
        adminService.clubStatusExcel(response)
        return ResponseEntity.ok().build()
    }
}