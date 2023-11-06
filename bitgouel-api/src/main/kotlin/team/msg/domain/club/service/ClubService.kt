package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.school.model.School

interface ClubService {
    fun queryAllClubsService(school: School): AllClubResponse
}