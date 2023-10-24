package team.msg.domain.professor.exception.constant

enum class ProfessorErrorCode(
    val message: String,
    val status: Int
) {
    PROFESSOR_NOT_FOUND("존재하지 않는 대학 교수입니다.", 404)
}