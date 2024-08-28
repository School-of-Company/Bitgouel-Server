package team.msg.global.config.properties

enum class CacheProperties(
    val cacheName: String,
    val maximumSize: Long,
    val expiredAfterWrite: Long
) {
    CLUB_PROFILE("queryClubs", 100L, 30L),
    UNIVERSITY_PROFILE("queryUniversities", 100L, 30L),
    SCHOOL_PROFILE("querySchools", 100L, 30L),
    COMPANY_PROFILE("queryCompanies", 100L, 30L),
    GOVERNMENT_PROFILE("queryGovernments", 100L, 30L)
}