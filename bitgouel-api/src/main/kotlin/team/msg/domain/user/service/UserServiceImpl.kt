package team.msg.domain.user.service

import org.springframework.stereotype.Service
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.model.Club
import team.msg.domain.company.exception.CompanyNotFoundException
import team.msg.domain.company.repository.CompanyInstructorRepository
import team.msg.domain.government.GovernmentNotFoundException
import team.msg.domain.government.repository.GovernmentRepository
import team.msg.domain.professor.exception.ProfessorNotFoundException
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.global.exception.InvalidRoleException

@Service
class UserServiceImpl(
    private val userUtil: UserUtil,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentRepository: GovernmentRepository,
    private val adminRepository: AdminRepository
) : UserService {
    /**
     * 현재 로그인한 유저의 마이페이지를 조회하는 비지니스 로직입니다.
     * @return 조회한 마이페이지 정보가 담긴 dto
     */
    override fun queryUserPageService(): UserPageResponse {
        val user = userUtil.queryCurrentUser()

        val organization = when(user.authority){
            Authority.ROLE_STUDENT -> {
                val student = findStudent(user)
                val club = student.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리 ${student.grade}학년 ${student.classRoom}반 ${student.number}번"
            }
            Authority.ROLE_TEACHER -> {
                val teacher = findTeacher(user)
                val club = teacher.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리"
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = findBbozzak(user)
                val club = bbozzak.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리"
            }
            Authority.ROLE_PROFESSOR -> {
                val professor = findProfessor(user)
                "${professor.university}"
            }
            Authority.ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = findCompanyInstructor(user)
                "${companyInstructor.company}"
            }
            Authority.ROLE_GOVERNMENT -> {
                val government = findGovernment(user)
                "${government.governmentName}"
            }
            Authority.ROLE_ADMIN -> {
                val admin = findAdmin(user)
                "교육청"
            }
            else -> throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority}]")
        }

        return UserResponse.pageOf(user, organization)
    }

    private fun findStudent(user: User) = studentRepository.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findTeacher(user: User) = teacherRepository.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findBbozzak(user: User) = bbozzakRepository.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다.  info : [ userId = ${user.id} ]")

    private fun findProfessor(user: User) = professorRepository.findByUser(user)
        ?: throw ProfessorNotFoundException("대학 교수를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findCompanyInstructor(user: User) = companyInstructorRepository.findByUser(user)
        ?: throw CompanyNotFoundException("기업 강사를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findGovernment(user: User) = governmentRepository.findByUser(user)
        ?: throw GovernmentNotFoundException("유관기관을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findAdmin(user: User) = adminRepository.findByUser(user)
        ?: throw AdminNotFoundException("어드민을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findSchool(club: Club) = club.school.highSchool
}