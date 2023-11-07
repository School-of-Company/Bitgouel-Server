package team.msg.common.util

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
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
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
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

    fun findStudentByUser(user: User) = studentRepository.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    fun findTeacherByUser(user: User) = teacherRepository.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    fun findBbozzakByUser(user: User) = bbozzakRepository.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다.  info : [ userId = ${user.id} ]")

    fun findProfessorByUser(user: User) = professorRepository.findByUser(user)
        ?: throw ProfessorNotFoundException("대학 교수를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    fun findCompanyInstructorByUser(user: User) = companyInstructorRepository.findByUser(user)
        ?: throw CompanyNotFoundException("기업 강사를 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    fun findGovernmentByUser(user: User) = governmentRepository.findByUser(user)
        ?: throw GovernmentNotFoundException("유관기관을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    fun findAdminByUser(user: User) = adminRepository.findByUser(user)
        ?: throw AdminNotFoundException("어드민을 찾을 수 없습니다. info : [ userId = ${user.id} ]")
}