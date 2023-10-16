package bitgouel.team.msg.domain.auth.presentation.data.request

import team.msg.domain.school.enums.HighSchool

class StudentSignUpRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: HighSchool,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val admissionNumber: Int
)