package team.msg.domain.university.service

import team.msg.domain.university.presentation.data.request.CreateUniversityRequest

interface UniversityService {
    fun createUniversity(request: CreateUniversityRequest)
}