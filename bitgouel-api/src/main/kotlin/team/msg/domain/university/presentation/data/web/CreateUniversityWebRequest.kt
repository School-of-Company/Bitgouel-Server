package team.msg.domain.university.presentation.data.web

data class CreateUniversityWebRequest(
    val id: Long,
    val department: String,
    val universityName: String
)