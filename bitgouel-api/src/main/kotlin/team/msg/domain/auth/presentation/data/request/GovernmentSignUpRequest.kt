package team.msg.domain.auth.presentation.data.request

data class GovernmentSignUpRequest(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val password: String,
    val highSchool: String,
    val clubName: String,
    val governmentName: String,
    val position: String,
    val sectors: String
)