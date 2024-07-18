package team.msg.domain.club.presentation.web.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field

class UpdateClubWebRequest(

    @NotBlank
    val name: String,

    @NotNull
    val field: Field

)