package team.msg.domain.auth.presentation.data.request

import team.msg.domain.school.enums.HighSchool

data class StudentSignUpRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: HighSchool,
    val clubName: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val admissionNumber: Int
)