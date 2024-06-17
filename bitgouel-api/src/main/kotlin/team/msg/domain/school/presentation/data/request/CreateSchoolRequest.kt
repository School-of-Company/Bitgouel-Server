package team.msg.domain.school.presentation.data.request

import org.springframework.web.multipart.MultipartFile

class CreateSchoolRequest(
    val schoolName: String,
    val logoImage: MultipartFile
)