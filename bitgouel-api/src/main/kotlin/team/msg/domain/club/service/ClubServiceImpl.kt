package team.msg.domain.club.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.presentation.data.response.*
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.repository.StudentRepository

@Service
class ClubServiceImpl(
    private val clubRepository: ClubRepository,
    private val schoolRepository: SchoolRepository,
    private val studentRepository: StudentRepository
) : ClubService {

    /**
     * 모든 동아리를 조회하는 비즈니스 로직
     * @param 동아리를 조회하기 위한 학교 이름
     */
    @Transactional(readOnly = true)
    override fun queryAllClubsService(highSchool: HighSchool): AllClubResponse {
        val school = schoolRepository.findByHighSchool(highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교 입니다. info : [ highSchool = $highSchool ]")

        val clubs = clubRepository.findAllBySchool(school)

        val response = AllClubResponse(
            ClubResponse.listOf(clubs)
        )

        return response
    }

    /**
     * 동아리를 상세 조회하는 비즈니스 로직
     * @param 동아리를 상세 조회하기 위한 id
     */
    @Transactional(readOnly = true)
    override fun queryClubDetailsService(id: Long): ClubDetailsResponse {
        val club = clubRepository.findByIdOrNull(id)
            ?: throw ClubNotFoundException("존재하지 않는 동아리 입니다. info : [ clubId = $id ]")

        val headCount = studentRepository.countByClub(club).toInt()

        val response = ClubResponse.detailOf(club, club.school.highSchool, headCount)

        return response
    }

    /**
     * 동아리를의 학생 리스트를 조회하는 비즈니스 로직
     * @param 동아리에 속한 학생 리스트를 조회하기 위한 id
     */
    override fun queryAllStudentsByClubId(id: Long): QueryAllStudentsByClubIdResponse {
        val club = clubRepository.findByIdOrNull(id)
            ?: throw ClubNotFoundException("존재하지 않는 동아리 입니다. info : [ clubId = $id ]")

        val students = studentRepository.findAllByClub(club)

        return QueryAllStudentsByClubIdResponse(
            ClubResponse.listOfStudent(students)
        )
    }
}