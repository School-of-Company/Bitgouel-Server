package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.school.enums.HighSchool

interface ClubService {
    fun queryAllClubsService(highSchool: HighSchool): AllClubResponse
}