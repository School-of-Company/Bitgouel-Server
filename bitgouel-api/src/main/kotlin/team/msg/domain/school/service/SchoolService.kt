package team.msg.domain.school.service

import team.msg.domain.school.presentation.data.response.SchoolsResponse

interface SchoolService {
    fun querySchools(): SchoolsResponse
}