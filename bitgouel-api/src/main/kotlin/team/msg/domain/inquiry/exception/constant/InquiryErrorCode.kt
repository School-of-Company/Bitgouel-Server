package team.msg.domain.inquiry.exception.constant

enum class InquiryErrorCode(
    val status: Int
) {
    NOT_FOUND_INQUIRY(404),
    NOT_FOUND_INQUIRY_ANSWER(404)
}