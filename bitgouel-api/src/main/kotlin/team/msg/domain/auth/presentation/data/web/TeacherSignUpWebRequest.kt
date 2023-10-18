package team.msg.domain.auth.presentation.data.web

import team.msg.domain.school.enums.HighSchool

data class TeacherSignUpWebRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: HighSchool,
    val clubName: String
)