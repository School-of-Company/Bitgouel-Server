package team.msg.domain.school.presentation.web

import javax.validation.constraints.NotBlank
import org.springframework.web.multipart.MultipartFile

class CreateSchoolWebRequest(

    @field:NotBlank
    val schoolName: String,

    val logoImage: MultipartFile

)