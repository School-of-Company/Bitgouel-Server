package team.msg.domain.club.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.exception.AlreadyExistClubException
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.exception.NotEmptyClubException
import team.msg.domain.club.model.Club
import team.msg.domain.club.presentation.data.reqeust.CreateClubRequest
import team.msg.domain.club.presentation.data.reqeust.UpdateClubRequest
import team.msg.domain.club.presentation.data.response.*
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.company.exception.CompanyInstructorNotFoundException
import team.msg.domain.company.model.CompanyInstructor
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.exception.GovernmentInstructorNotFoundException
import team.msg.domain.government.model.GovernmentInstructor
import team.msg.domain.government.repository.GovernmentInstructorRepository
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import team.msg.domain.student.presentation.data.response.StudentResponse
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.university.exception.ProfessorNotFoundException
import team.msg.domain.university.model.Professor
import team.msg.domain.university.repository.ProfessorRepository
import team.msg.domain.user.model.User
import team.msg.global.exception.InvalidRoleException
import java.util.*

@Service
class ClubServiceImpl(
    private val clubRepository: ClubRepository,
    private val schoolRepository: SchoolRepository,
    private val studentRepository: StudentRepository,
    private val userUtil: UserUtil,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentInstructorRepository: GovernmentInstructorRepository
) : ClubService {

    /**
     * 모든 동아리를 조회하는 비즈니스 로직
     * @param 동아리를 조회하기 위한 학교 이름
     * @return 학교에 있는 취업동아리 리스트
     */
    @Transactional(readOnly = true)
    override fun queryClubs(highSchool: String): ClubsResponse {
        val school = schoolRepository.findByName(highSchool)
            ?: throw SchoolNotFoundException("존재하지 않는 학교 입니다. info : [ highSchool = $highSchool ]")

        val clubs = clubRepository.findAllBySchool(school)

        val response = ClubsResponse(
            ClubResponse.listOf(clubs)
        )

        return response
    }

    /**
     * 모든 동아리 이름을 조회하는 비즈니스 로직
     * @param 동아리 이름을 조회하기 위한 학교 id
     * @return 동아리 이름 리스트
     */
    @Transactional(readOnly = true)
    @Cacheable(value = ["queryClubs"])
    override fun queryClubNames(schoolName: String?): ClubNamesResponse {
        val clubs = clubRepository.findAllBySchoolName(schoolName)

        val response = ClubNamesResponse(
            ClubResponse.nameListOf(clubs)
        )

        return response
    }

    /**
     * 동아리를 상세 조회하는 비즈니스 로직
     * @param 동아리를 상세 조회하기 위한 id
     * @return 동아리 상세 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun queryClubDetailsById(id: Long): ClubDetailsResponse {
        val club = clubRepository.findByIdOrNull(id)
            ?: throw ClubNotFoundException("존재하지 않는 동아리 입니다. info : [ clubId = $id ]")

        val students = studentRepository.findAllByClub(club)

        val teacher = teacherRepository.findByClub(club)
            ?: throw TeacherNotFoundException("동아리를 전담하고 있는 선생님이 없습니다. info : [ clubId = ${club.id} ]")

        val response = ClubResponse.detailOf(club, students, teacher)

        return response
    }

    /**
     * 동아리를 상세 조회하는 비즈니스 로직
     * @return 동아리 상세 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun queryMyClubDetails(): ClubDetailsResponse {
        val user = userUtil.queryCurrentUser()

        val entity = userUtil.getAuthorityEntityAndOrganization(user).first

        val club = when(entity) {
            is Student -> findStudentByUser(user).club
            is Teacher -> findTeacherByUser(user).club
            is Bbozzak -> findBbozzakByUser(user).club
            is Professor -> findProfessorByUser(user).club
            is CompanyInstructor -> findCompanyInstructorByUser(user).club
            is GovernmentInstructor -> findGovernmentByUser(user).club
            else ->  throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority} ]")
        }

        val students = studentRepository.findAllByClub(club)

        val teacher = teacherRepository.findByClub(club)
            ?: throw TeacherNotFoundException("동아리를 전담하고 있는 선생님이 없습니다. info : [ clubId = ${club.id} ]")

        val response = ClubResponse.detailOf(club, students, teacher)

        return response
    }

    /**
     * 동아리에 소속된 학생의 상세정보를 조회하는 비즈니스 로직
     * @param 동아리 id, 동아리에 속한 상세정보를 조회할 학생 id
     * @return 동아리에 속한 학생의 상세정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun queryStudentDetails(clubId: Long, studentId: UUID): StudentDetailsResponse {
        val club = clubRepository.findByIdOrNull(clubId)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. info : [ clubId = $clubId ]")

        val student = studentRepository.findByIdAndClub(studentId, club)
            ?: throw StudentNotFoundException("동아리에 존재하지 않는 학생입니다. info : [ clubId = $clubId, studentId = $studentId ]")

        val response = StudentResponse.detailOf(student)

        return response
    }

    /**
     * 동아리를 생성하는 비지니스 로직
     * @param 동아리가 속할 학교 id, 생성할 동아리 정보
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryClubs"])
    override fun createClub(schoolId: Long, request: CreateClubRequest) {
        val school = schoolRepository.findByIdOrNull(schoolId)
            ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info : [ id = $schoolId ]")

        if (clubRepository.existsByName(request.clubName)) {
            throw AlreadyExistClubException("이미 존재하는 동아리입니다. info : [ clubName = ${request.clubName} ]")
        }

        val club = Club(
            school = school,
            name = request.clubName,
            field = request.field
        )
        clubRepository.save(club)
    }

    /**
     * 동아리를 수정하는 비지니스 로직
     * @param 동아리 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryClubs"])
    override fun updateClub(id: Long, request: UpdateClubRequest) {
        val club = clubRepository.findByIdOrNull(id)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. info : [ clubId = $id ]")

        if (clubRepository.existsByNameAndIdNotLike(request.clubName, id)) {
            throw AlreadyExistClubException("이미 존재하는 동아리입니다. info : [ clubName = ${request.clubName} ]")
        }

        val updateClub = Club(
            id = id,
            school = club.school,
            name = request.clubName,
            field = request.field
        )
        clubRepository.save(updateClub)
    }

    /**
     * 동아리를 삭제하는 비지니스 로직
     * @param 동아리 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @CacheEvict(value = ["queryClubs"])
    override fun deleteClub(id: Long) {
        val club = clubRepository.findByIdOrNull(id)
            ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. info : [ clubId = $id ]")

        if (isAnyUserBelong(club))
            throw NotEmptyClubException("동아리에 탈퇴하지 않은 유저가 있습니다. info : [ clubId = $id ]")

        clubRepository.deleteById(club.id)
    }

    private fun isAnyUserBelong(club: Club): Boolean =
        teacherRepository.existsByClub(club) ||
        bbozzakRepository.existsByClub(club) ||
        studentRepository.existsByClub(club) ||
        professorRepository.existsByClub(club) ||
        governmentInstructorRepository.existsByClub(club) ||
        companyInstructorRepository.existsByClub(club)

    private fun findStudentByUser(user: User) = studentRepository.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findTeacherByUser(user: User) = teacherRepository.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findBbozzakByUser(user: User) = bbozzakRepository.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다.  info : [ userId = ${user.id} ]")

    private fun findProfessorByUser(user: User) = professorRepository.findByUser(user)
        ?: throw ProfessorNotFoundException("대학 교수를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findCompanyInstructorByUser(user: User) = companyInstructorRepository.findByUser(user)
        ?: throw CompanyInstructorNotFoundException("기업 강사를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findGovernmentByUser(user: User) = governmentInstructorRepository.findByUser(user)
        ?: throw GovernmentInstructorNotFoundException("유관기관을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

}