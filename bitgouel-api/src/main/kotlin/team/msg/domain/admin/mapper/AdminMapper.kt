package team.msg.domain.admin.mapper

import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest

interface AdminMapper {
    fun queryUsersWebRequestToDto(webRequest: QueryUsersWebRequest): QueryUsersRequest
}