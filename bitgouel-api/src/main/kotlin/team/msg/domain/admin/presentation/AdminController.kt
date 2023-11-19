package team.msg.domain.admin.presentation

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.admin.mapper.AdminMapper
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest
import team.msg.domain.admin.service.AdminService
import team.msg.domain.user.presentation.data.response.UsersResponse

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val adminMapper: AdminMapper
) {
    @GetMapping
    fun queryUser(webRequest: QueryUsersWebRequest,pageable: Pageable): ResponseEntity<UsersResponse> {
        val request = adminMapper.queryUsersWebRequestToDto(webRequest)
        val response = adminService.queryUsers(request, pageable)
        return ResponseEntity.ok(response)
    }

}