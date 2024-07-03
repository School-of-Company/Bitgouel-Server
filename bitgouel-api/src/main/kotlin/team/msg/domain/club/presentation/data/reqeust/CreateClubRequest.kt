package team.msg.domain.club.presentation.data.reqeust

import team.msg.common.enums.Field

data class CreateClubRequest(
    val clubName: String,
    val field: Field
)