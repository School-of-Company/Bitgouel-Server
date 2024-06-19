package team.msg.domain.company.exception.constant

enum class CompanyErrorCode(
    val status: Int
) {
    COMPANY_NOT_FOUND(404),
    COMPANY_INSTRUCTOR_NOT_FOUND(404)
}