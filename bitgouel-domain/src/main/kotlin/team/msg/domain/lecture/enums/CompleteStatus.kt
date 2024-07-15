package team.msg.domain.lecture.enums

enum class CompleteStatus(
    val description: String
) {
    NOT_COMPLETED_YET("아직 이수하지 않음"),
    COMPLETED_IN_3RD("이수 완료(3학년)"),
    COMPLETED_IN_2RD("이수 완료(2학년)"),
    COMPLETED_IN_1RD("이수 완료(1학년)")
}