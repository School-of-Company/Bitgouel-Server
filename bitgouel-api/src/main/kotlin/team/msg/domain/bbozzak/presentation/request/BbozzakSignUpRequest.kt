package team.msg.domain.bbozzak.presentation.request

import team.msg.domain.school.enums.HighSchool

data class BbozzakSignUpRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: HighSchool,
    val clubName: String
)