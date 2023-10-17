package team.msg.domain.auth.presentation.data.request

import javax.persistence.EnumType
import javax.persistence.Enumerated
import team.msg.domain.school.enums.HighSchool

class StudentSignUpRequest(
    val email: String,

    val name: String,

    val phoneNumber: String,

    val password: String,

    @Enumerated(EnumType.STRING)
    val highSchool: HighSchool,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val admissionNumber: Int
)