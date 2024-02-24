package team.msg.domain.admin.mapper

import org.springframework.stereotype.Component
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.admin.presentation.data.web.QueryUsersWebRequest

@Component
class AdminMapperImpl : AdminMapper {
    /**
     * User 조회 및 검색 Web Request를 애플리케이션 영역에서 사용될 Dto 로 매핑합니다.
     */
    override fun queryUsersWebRequestToDto(webRequest: QueryUsersWebRequest): QueryUsersRequest =
        QueryUsersRequest(
            keyword = webRequest.keyword,
            authority = webRequest.authority,
            approveStatus = webRequest.approveStatus
        )
}