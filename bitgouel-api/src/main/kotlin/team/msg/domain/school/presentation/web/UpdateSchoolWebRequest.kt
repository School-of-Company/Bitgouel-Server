package team.msg.domain.school.presentation.web

import org.springframework.web.multipart.MultipartFile

class UpdateSchoolWebRequest(
    val schoolName: String,
    val logoImage: MultipartFile
)