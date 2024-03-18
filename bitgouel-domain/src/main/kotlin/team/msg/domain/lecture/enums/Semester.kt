package team.msg.domain.lecture.enums

/**
 * FIRST_YEAR_FALL_SEMESTER 1학년 2학기
 * SECOND_YEAR_SPRING_SEMESTER 2학년 1학기
 * SECOND_YEAR_FALL_SEMESTER 2학년 2학기
 * THIRD_YEAR_SPRING_SEMESTER 3학년 1학기
 */
enum class Semester(
    val yearAndSemester: String
) {
    FIRST_YEAR_FALL_SEMESTER("1-2"),
    SECOND_YEAR_SPRING_SEMESTER("2-1"),
    SECOND_YEAR_FALL_SEMESTER("2-2"),
    THIRD_YEAR_SPRING_SEMESTER("3-1")
}