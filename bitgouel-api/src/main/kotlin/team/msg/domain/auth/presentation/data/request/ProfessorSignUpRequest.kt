package team.msg.domain.auth.presentation.data.request

import team.msg.domain.school.enums.HighSchool

data class ProfessorSignUpRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: HighSchool,
    val clubName: String,
    val university: String
)