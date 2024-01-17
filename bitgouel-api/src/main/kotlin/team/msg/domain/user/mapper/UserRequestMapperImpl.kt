package team.msg.domain.user.mapper

import org.springframework.stereotype.Component
import team.msg.domain.user.presentation.data.request.ModifyPasswordRequest
import team.msg.domain.user.presentation.web.ModifyPasswordWebRequest

@Component
class UserRequestMapperImpl : UserRequestMapper {
    override fun modifyPasswordWebRequestToDto(webRequest: ModifyPasswordWebRequest): ModifyPasswordRequest =
        ModifyPasswordRequest(
            currentPassword = webRequest.currentPassword,
            newPassword = webRequest.newPassword
        )
}