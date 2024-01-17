package team.msg.domain.user.mapper

import team.msg.domain.user.presentation.data.request.ModifyPasswordRequest
import team.msg.domain.user.presentation.web.ModifyPasswordWebRequest

interface UserRequestMapper {
    fun modifyPasswordWebRequestToDto(webRequest: ModifyPasswordWebRequest): ModifyPasswordRequest
}