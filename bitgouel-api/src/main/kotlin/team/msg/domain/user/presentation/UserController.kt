package team.msg.domain.user.presentation

import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.user.mapper.UserRequestMapper
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.presentation.web.ModifyPasswordWebRequest
import team.msg.domain.user.service.UserService

@RestController
@RequestMapping("/user")
class UserController (
    private val userService: UserService,
    private val userRequestMapper: UserRequestMapper
) {
    @GetMapping
    fun queryUserPage(): ResponseEntity<UserPageResponse> {
        val response = userService.queryUserPageService()
        return ResponseEntity.ok(response)
    }

    @PatchMapping
    fun modifyPassword(@RequestBody @Valid webRequest: ModifyPasswordWebRequest): ResponseEntity<Void> {
        val request = userRequestMapper.modifyPasswordWebRequestToDto(webRequest)
        userService.modifyPasswordService(request)
        return ResponseEntity.noContent().build()
    }
}