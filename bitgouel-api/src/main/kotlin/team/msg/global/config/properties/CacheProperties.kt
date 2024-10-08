package team.msg.global.config.properties

enum class CacheProperties(
    val cacheName: String,
    val maximumSize: Long,
    val expiredAfterWrite: Long
) {
    CLUB_PROFILE("queryClubs", 50L, 30L),
    UNIVERSITY_PROFILE("queryUniversities", 2L, 60L),
    SCHOOL_PROFILE("querySchools", 2L, 60L),
    COMPANY_PROFILE("queryCompanies", 2L, 60L),
    GOVERNMENT_PROFILE("queryGovernments", 2L, 60L)
}