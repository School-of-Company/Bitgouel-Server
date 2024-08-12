package team.msg.domain.admin.service

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import team.msg.common.enums.ApproveStatus
import team.msg.common.util.StudentUtil
import team.msg.common.util.UserUtil
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.InvalidEmailException
import team.msg.domain.user.exception.InvalidPasswordException
import team.msg.domain.user.exception.InvalidPhoneNumberException
import team.msg.domain.user.exception.UserAlreadyApprovedException
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import team.msg.domain.user.repository.UserRepository
import java.util.*

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository,
    private val userUtil: UserUtil,
    private val studentUtil: StudentUtil,
    private val clubRepository: ClubRepository,
    private val studentRepository: StudentRepository
) : AdminService {

    /**
     * 유저를 전체 조회 및 이름, 역할, 승인 상태들로 조회하는 비즈니스 로직입니다
     * @param 유저를 검색할 조건으로 keyword, authority, approveStatus
     * @return 학생 정보를 리스트로 담은 Dto
     */
    @Transactional(readOnly = true)
    override fun queryUsers(request: QueryUsersRequest): UsersResponse {
        val users = userRepository.query(request.keyword, request.authority, request.approveStatus)

        return UsersResponse(
            users.map { user ->
                val student = studentRepository.findByUser(user)
                UserResponse.of(user, student)
            }
        )
    }

    /**
     * 회원가입 대기 중인 유저를 승인하는 비즈니스 로직입니다
     * @param 승인할 유저들을 검색하기 위한 userIds
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun approveUsers(userIds: List<UUID>) {
        val users = userRepository.findByIdIn(userIds)

        val approvedUsers = users.map {
            if (it.approveStatus == ApproveStatus.APPROVED) {
                throw UserAlreadyApprovedException("이미 승인된 유저입니다. Info : [ userId = ${it.id} ]")
            }

            User(
                id = it.id,
                email = it.email,
                name = it.name,
                phoneNumber = it.phoneNumber,
                password = it.password,
                authority = it.authority,
                approveStatus = ApproveStatus.APPROVED
            )
        }

        userRepository.saveAll(approvedUsers)
    }

    /**
     * 회원가입 대기 중인 유저를 거절하는 비즈니스 로직입니다
     * @param 거절할 유저들을 검색하기 위한 userIds
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun rejectUsers(userIds: List<UUID>) {
        val users = userRepository.findByIdIn(userIds)

        users.forEach {

            if(it.approveStatus == ApproveStatus.APPROVED)
                throw UserAlreadyApprovedException("이미 승인된 유저입니다. Info : [ userId = ${it.id} ]")

            userUtil.withdrawUser(it)
        }

        userRepository.deleteByIdIn(userIds)
    }

    /**
     * 유저를 강제 탈퇴 시키는 비지니스 로직입니다
     * @param 유저를 삭제하기 위한 userIds
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun forceWithdraw(userIds: List<UUID>) {
        val users = userRepository.findByIdIn(userIds)

        users.forEach { userUtil.withdrawUser(it) }

        userRepository.deleteByIdIn(userIds)
    }

    /**
     * 학생 리스트 엑셀을 업로드 하는 비지니스 로직입니다
     * @param 학생 리스트 엑셀 업로드를 위한 MultipartFile
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun uploadStudentListExcel(file: MultipartFile) {
        file.inputStream.use {
            val workbook = WorkbookFactory.create(file.inputStream)

            val sheet = workbook.getSheetAt(0)

            sheet.forEachIndexed { index, row ->
                if (index == 0)
                    return@forEachIndexed

                if (row.getCell(0).stringCellValue == "")
                    return

                val email = row.getCell(0).stringCellValue
                val name = row.getCell(1).stringCellValue
                val phoneNumber = row.getCell(2).stringCellValue
                val password = row.getCell(3).stringCellValue
                val clubName = row.getCell(4).stringCellValue
                val grade = row.getCell(5).numericCellValue.toInt()
                val classRoom = row.getCell(6).numericCellValue.toInt()
                val number = row.getCell(7).numericCellValue.toInt()
                val admissionNumber = row.getCell(8).numericCellValue.toInt()
                val subscriptionGrade = row.getCell(9).numericCellValue.toInt()

                validateExcelStudentData(email, phoneNumber, password)

                val user = userUtil.createUser(email, name, phoneNumber, password, Authority.ROLE_STUDENT)

                val club = clubRepository findByName clubName

                studentUtil.createStudent(user, club, grade, classRoom, number, admissionNumber, subscriptionGrade)
            }
        }
    }

    private fun validateExcelStudentData(email: String, phoneNumber: String, password: String) {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        if (!email.matches(emailRegex))
            throw InvalidEmailException("유효하지 않은 이메일입니다. [ email =  $email ]")

        val phoneRegex = "^010[0-9]{8}\$".toRegex()
        if (!phoneNumber.matches(phoneRegex))
            throw InvalidPhoneNumberException("유효하지 않은 휴대폰 번호입니다. [ phoneNumber = $phoneNumber ]")

        val passwordRegex = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$".toRegex()
        if (!password.matches(passwordRegex))
            throw InvalidPasswordException("유효하지 않은 비밀번호입니다. [ password = $password ]")
    }

    private infix fun ClubRepository.findByName(clubName: String): Club =
        this.findByName(clubName) ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. Info [ clubName = $clubName ]")

}