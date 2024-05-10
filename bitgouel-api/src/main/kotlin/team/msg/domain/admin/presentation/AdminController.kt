package team.msg.domain.admin.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.admin.mapper.AdminMapper
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest
import team.msg.domain.admin.service.AdminService
import team.msg.domain.user.presentation.data.response.UserDetailsResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import java.io.File
import java.util.*

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
    fun approveUsers(@RequestParam userIds: List<UUID>): ResponseEntity<Void> {
        adminService.approveUsers(userIds)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/reject")
    fun rejectUsers(@RequestParam userIds: List<UUID>): ResponseEntity<Void> {
        adminService.rejectUsers(userIds)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{user_id}")
    @Deprecated("This API is deprecated. Use query user API instead")
    fun queryUserDetails(@PathVariable("user_id") userId: UUID): ResponseEntity<UserDetailsResponse> {
        val response = adminService.queryUserDetails(userId)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/withdraw")
    fun forceWithdraw(@RequestParam userIds: List<UUID>): ResponseEntity<Void> {
        adminService.forceWithdraw(userIds)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/excel")
    fun uploadStudentListExcel(@RequestPart file: File): ResponseEntity<Void> {
        adminService.uploadStudentListExcel(file)
        return ResponseEntity.status(HttpStatus.OK).build()
    }
}