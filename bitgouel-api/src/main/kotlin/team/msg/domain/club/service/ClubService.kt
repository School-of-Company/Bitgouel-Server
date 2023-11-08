package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.QueryAllUsersByClubIdResponse
import team.msg.domain.school.enums.HighSchool

interface ClubService {
    fun queryAllClubsService(highSchool: HighSchool): AllClubResponse
    fun queryClubDetailsService(id: Long): ClubDetailsResponse
    fun queryAllUsersByClubId(id: Long): QueryAllUsersByClubIdResponse
}