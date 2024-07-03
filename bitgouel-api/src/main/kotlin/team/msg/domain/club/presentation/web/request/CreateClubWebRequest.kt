package team.msg.domain.club.presentation.web.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import team.msg.common.enums.Field

data class CreateClubWebRequest(
    
    @NotBlank
    val clubName: String,
    
    @NotNull
    val field: Field
    
)