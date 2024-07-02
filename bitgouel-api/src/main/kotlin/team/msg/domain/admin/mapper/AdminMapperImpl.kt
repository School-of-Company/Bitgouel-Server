package team.msg.domain.admin.mapper

import org.springframework.stereotype.Component
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest

@Component
class AdminMapperImpl : AdminMapper {
    override fun queryUsersWebRequestToDto(webRequest: QueryUsersWebRequest): QueryUsersRequest =
        QueryUsersRequest(
            keyword = webRequest.keyword,
            authority = webRequest.authority,
            approveStatus = webRequest.approveStatus
        )
}