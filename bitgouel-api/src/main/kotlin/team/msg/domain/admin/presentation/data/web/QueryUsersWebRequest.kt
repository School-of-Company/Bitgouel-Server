package team.msg.domain.admin.presentation.data.web

import org.springframework.web.bind.annotation.RequestParam
import team.msg.domain.user.enums.Authority

class QueryUsersWebRequest(
    @RequestParam(required = false)
    val keyword: String = "",

    @RequestParam(required = false)
    val authority: Authority = Authority.ROLE_USER
)