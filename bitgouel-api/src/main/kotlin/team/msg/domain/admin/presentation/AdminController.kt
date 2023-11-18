package team.msg.domain.admin.presentation

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.admin.service.AdminService
import team.msg.domain.user.presentation.data.response.UsersResponse

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping
    fun queryUser(@RequestParam keyword: String = "", pageable: Pageable): ResponseEntity<UsersResponse> {
        val response = adminService.queryUsers(keyword, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

}