package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.student.presentation.data.response.AllStudentsResponse

interface ClubService {
    fun queryAllClubsService(highSchool: HighSchool): ClubsResponse
    fun queryClubDetailsService(id: Long): ClubDetailsResponse
    fun queryAllStudentsByClubId(id: Long): AllStudentsResponse
}