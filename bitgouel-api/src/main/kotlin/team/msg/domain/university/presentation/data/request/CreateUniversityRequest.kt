package team.msg.domain.university.presentation.data.request

data class CreateUniversityRequest(
    val id: Long,
    val department: String,
    val universityName: String
)