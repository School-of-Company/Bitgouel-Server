package team.msg.domain.club.exception.constant

enum class ClubErrorCode(
    val status: Int
) {
    NOT_EMPTY_CLUB(400),
    INVALID_FIELD(400),
    CLUB_NOT_FOUND(404),
    ALREADY_EXIST_CLUB(409)
}