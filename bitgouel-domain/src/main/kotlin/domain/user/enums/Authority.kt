package domain.user.enums

/**
 * 유저의 Authority를 정의하는 enum타입 클래스입니다.
 * USER - Default
 * ADMIN - 교육청, 어드민 권한
 * STUDENT - 학생
 * TEACHER - 취업 동아리 선생님
 * BBOZZAK - 뽀작 선생님
 * PROFESSOR - 대학 교수
 * COMPANY_INSTRUCTOR - 기업 강사
 * GOVERNMENT - 유관 기관
 */
enum class Authority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_STUDENT,
    ROLE_TEACHER,
    ROLE_BBOZZAK,
    ROLE_PROFESSOR,
    ROLE_COMPANY_INSTRUCTOR,
    ROLE_GOVERNMENT
}