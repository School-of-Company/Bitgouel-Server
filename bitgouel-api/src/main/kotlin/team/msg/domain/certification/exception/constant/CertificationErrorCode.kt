package team.msg.domain.certification.exception.constant

enum class CertificationErrorCode(
    val status: Int
) {
    FORBIDDEN_CERTIFICATION(403),
    ALREADY_ACQUIRED_CERTIFICATION(409)
}