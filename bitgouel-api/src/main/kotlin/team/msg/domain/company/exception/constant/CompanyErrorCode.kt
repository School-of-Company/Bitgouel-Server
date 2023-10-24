package team.msg.domain.company.exception.constant

enum class CompanyErrorCode(
    val message: String,
    val status: Int
) {
    COMPANY_NOT_FOUND("존재하지 않는 기업 강사 입니다.", 404)
}