package team.msg.domain.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.club.model.Club
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.presentation.data.response.UserPageResponse
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.global.exception.InvalidRoleException

@Service
class UserServiceImpl(
    private val userUtil: UserUtil
) : UserService {
    /**
     * 현재 로그인한 유저의 마이페이지를 조회하는 비지니스 로직입니다.
     * @return 조회한 마이페이지 정보가 담긴 dto
     */
    @Transactional(rollbackFor = [Exception::class], readOnly = true)
    override fun queryUserPageService(): UserPageResponse {
        val user = userUtil.queryCurrentUser()

        val organization = when(user.authority){
            Authority.ROLE_STUDENT -> {
                val student = userUtil.findStudentByUser(user)
                val club = student.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리 ${student.grade}학년 ${student.classRoom}반 ${student.number}번"
            }
            Authority.ROLE_TEACHER -> {
                val teacher = userUtil.findTeacherByUser(user)
                val club = teacher.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리"
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = userUtil.findBbozzakByUser(user)
                val club = bbozzak.club
                val school = findSchool(club)
                "${school.schoolName} ${club.name} 동아리"
            }
            Authority.ROLE_PROFESSOR -> {
                val professor = userUtil.findProfessorByUser(user)
                "${professor.university}"
            }
            Authority.ROLE_COMPANY_INSTRUCTOR -> {
                val companyInstructor = userUtil.findCompanyInstructorByUser(user)
                "${companyInstructor.company}"
            }
            Authority.ROLE_GOVERNMENT -> {
                val government = userUtil.findGovernmentByUser(user)
                "${government.governmentName}"
            }
            Authority.ROLE_ADMIN -> {
                val admin = userUtil.findAdminByUser(user)
                "교육청"
            }
            else -> throw InvalidRoleException("유효하지 않은 권한입니다. info : [ userAuthority = ${user.authority}]")
        }

        return UserResponse.pageOf(user, organization)
    }

    private fun findSchool(club: Club) = club.school.highSchool
}