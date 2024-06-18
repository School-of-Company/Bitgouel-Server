package team.msg.domain.university.exception.constant

enum class UniversityErrorCode(
    val status: Int
) {
    UNIVERSITY_NOT_FOUND(404),
    PROFESSOR_NOT_FOUND(404)
}