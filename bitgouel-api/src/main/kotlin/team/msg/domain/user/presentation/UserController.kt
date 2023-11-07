package team.msg.domain.user.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.service.UserService

@RestController
@RequestMapping("/user")
class UserController (
    private val userService: UserService
) {
    @GetMapping
    fun queryUserPage(): ResponseEntity<UserPageResponse> {
        val response = userService.queryUserPageService()
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}