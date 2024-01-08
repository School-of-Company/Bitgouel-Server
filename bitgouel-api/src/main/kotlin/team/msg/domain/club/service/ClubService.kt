package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import java.util.*

interface ClubService {
    fun queryAllClubsService(highSchool: HighSchool): ClubsResponse
    fun queryClubDetailsByIdService(id: Long): ClubDetailsResponse
    fun queryMyClubDetailsService(): ClubDetailsResponse
    fun queryStudentDetails(clubId: Long, studentId: UUID): StudentDetailsResponse
}