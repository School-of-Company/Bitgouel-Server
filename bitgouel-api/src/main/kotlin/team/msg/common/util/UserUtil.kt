package team.msg.common.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import team.msg.common.entity.BaseUUIDEntity
import team.msg.domain.admin.exception.AdminNotFoundException
import team.msg.domain.admin.repository.AdminRepository
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.repository.BbozzakRepository
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
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import team.msg.global.exception.InvalidRoleException
import team.msg.global.security.principal.AuthDetails
import java.util.*

@Component
class UserUtil(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val professorRepository: ProfessorRepository,
    private val companyInstructorRepository: CompanyInstructorRepository,
    private val governmentRepository: GovernmentRepository,
    private val adminRepository: AdminRepository
) {
    fun queryCurrentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal

        val userId = UUID.fromString(if(principal is AuthDetails)
            principal.username
        else
            principal.toString())

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("존재하지 않는 유저입니다. : [ id = $userId ]")

        return user
    }

    /**
     * 유저의 권한에 따라 권한 엔티티를 반환하는 함수입니다.
     * @param 권한 엔티티를 얻고자 하는 유저
     * @return 유저에 따른 권한 엔티티
     */
    fun getAuthorityEntityAndOrganization(user: User): Pair<BaseUUIDEntity, String> {
        return when (user.authority) {
            Authority.ROLE_STUDENT -> {
                val student = findStudentByUser(user)
                val club = student.club
                val school = club.school
                val organization = "${school.highSchool.schoolName} ${club.name} 동아리 ${student.grade}학년 ${student.classRoom}반 ${student.number}번"
                Pair(student, organization)
            }
            Authority.ROLE_TEACHER -> {
                val teacher = findTeacherByUser(user)
                val club = teacher.club
                val school = club.school
                val organization = "${school.highSchool.schoolName} ${club.name} 동아리"
                Pair(teacher, organization)
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = findBbozzakByUser(user)
                val club = bbozzak.club
                val school = club.school
                val organization = "${school.highSchool.schoolName} ${club.name} 동아리"
                Pair(bbozzak, organization)
            }
            Authority.ROLE_PROFESSOR -> {
                val professor = findProfessorByUser(user)
                val organization = professor.university
                Pair(professor, organization)
            }
            Authority.ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = findCompanyInstructorByUser(user)
                val organization = companyInstructor.company
                Pair(companyInstructor, organization)
            }
            Authority.ROLE_GOVERNMENT -> {
                val government = findGovernmentByUser(user)
                val organization = government.governmentName
                Pair(government, organization)
            }
            Authority.ROLE_ADMIN -> {
                val admin = findAdminByUser(user)
                val organization = "교육청"
                Pair(admin, organization)
            }

            else -> throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority}]")
        }
    }

    private fun findStudentByUser(user: User) = studentRepository.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findTeacherByUser(user: User) = teacherRepository.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findBbozzakByUser(user: User) = bbozzakRepository.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다.  info : [ userId = ${user.id} ]")

    private fun findProfessorByUser(user: User) = professorRepository.findByUser(user)
        ?: throw ProfessorNotFoundException("대학 교수를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findCompanyInstructorByUser(user: User) = companyInstructorRepository.findByUser(user)
        ?: throw CompanyNotFoundException("기업 강사를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findGovernmentByUser(user: User) = governmentRepository.findByUser(user)
        ?: throw GovernmentNotFoundException("유관기관을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private fun findAdminByUser(user: User) = adminRepository.findByUser(user)
        ?: throw AdminNotFoundException("어드민을 찾을 수 없습니다. info : [ userId = ${user.id} ]")
}