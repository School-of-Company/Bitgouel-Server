package team.msg.domain.university.service

import team.msg.domain.university.presentation.data.response.UniversitiesResponse
import team.msg.domain.university.presentation.data.request.CreateUniversityRequest
import java.util.*

interface UniversityService {
    fun createUniversity(request: CreateUniversityRequest)
    fun deleteUniversity(id: Long)
    fun queryUniversities(): UniversitiesResponse
}