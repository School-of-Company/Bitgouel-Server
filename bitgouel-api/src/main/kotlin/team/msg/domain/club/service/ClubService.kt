package team.msg.domain.club.service

import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubNamesResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import java.util.*

interface ClubService {
    fun queryClubs(highSchool: String): ClubsResponse
    fun queryClubNames(schoolName: String?): ClubNamesResponse
    fun queryClubDetailsById(id: Long): ClubDetailsResponse
    fun queryMyClubDetails(): ClubDetailsResponse
    fun queryStudentDetails(clubId: Long, studentId: UUID): StudentDetailsResponse
    fun createClub(schoolId: Long, request: CreateClubRequest)
    fun updateClub(id: Long, request: UpdateClubRequest)
    fun deleteClub(id: Long)
}