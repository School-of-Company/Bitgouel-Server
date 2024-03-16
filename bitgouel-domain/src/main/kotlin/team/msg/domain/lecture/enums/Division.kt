package team.msg.domain.lecture.enums

/**
 * AUTOMOBILE_INDUSTRY 자동차 산업
 * ENERGY_INDUSTRY 에너지 산업
 * MEDICAL_HEALTHCARE 의료헬스케어
 * AI_CONVERGENCE AI 융복합
 * CULTURAL_INDUSTRY 문화산업
 */
enum class Division(
    val divisionName: String
) {
    AUTOMOBILE_INDUSTRY("자동차 산업"),
    ENERGY_INDUSTRY("에너지 산업"),
    MEDICAL_HEALTHCARE("의료 헬스 케어"),
    AI_CONVERGENCE("AI 융복합"),
    CULTURAL_INDUSTRY("문화 산업"),
}