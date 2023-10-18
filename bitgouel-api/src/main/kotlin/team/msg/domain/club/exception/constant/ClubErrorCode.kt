package team.msg.domain.club.exception.constant

enum class ClubErrorCode(
    val message: String,
    val status: Int
) {
    CLUB_NOT_FOUND("존재하지 않는 동아리입니다.", 404)
}