package team.msg.domain.admin.service

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import team.msg.common.enums.ApproveStatus
import team.msg.common.enums.Field
import team.msg.common.util.KakaoUtil
import team.msg.common.util.StudentUtil
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.InvalidCellTypeException
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.club.exception.AlreadyExistClubException
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.exception.InvalidFieldException
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.lecture.enums.Semester
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.model.LectureLocation
import team.msg.domain.lecture.repository.LectureDateRepository
import team.msg.domain.lecture.repository.LectureLocationRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.InvalidEmailException
import team.msg.domain.user.exception.InvalidPasswordException
import team.msg.domain.user.exception.InvalidPhoneNumberException
import team.msg.domain.user.exception.UserAlreadyApprovedException
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.presentation.data.response.UserResponse
import team.msg.domain.user.presentation.data.response.UsersResponse
import team.msg.domain.user.repository.UserRepository
import team.msg.global.exception.InternalServerException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository,
    private val userUtil: UserUtil,
    private val studentUtil: StudentUtil,
    private val clubRepository: ClubRepository,
    private val studentRepository: StudentRepository,
    private val schoolRepository: SchoolRepository,
    private val lectureRepository: LectureRepository,
    private val lectureDateRepository: LectureDateRepository,
    private val kakaoUtil: KakaoUtil,
    private val lectureLocationRepository: LectureLocationRepository
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
            val workbook = try {
                WorkbookFactory.create(file.inputStream)
            } catch (e: IndexOutOfBoundsException) {
                throw InvalidCellTypeException("전화번호 셀 서식을 텍스트로 바꿔주세요.")
            } catch (e: Exception) {
                throw InternalServerException("엑셀 파일 처리 중 문제가 발생했습니다. info : [ errorMessage = ${e.message} ]")
            }

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

    /**
     * 동아리 리스트 엑셀을 업로드 하는 비지니스 로직입니다
     * @param 동아리 리스트 엑셀 업로드를 위한 MultipartFile
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun uploadClubListExcel(file: MultipartFile) {
        file.inputStream.use {
            val workbook = try {
                WorkbookFactory.create(file.inputStream)
            } catch (e: Exception) {
                throw InternalServerException("엑셀 파일 처리 중 문제가 발생했습니다. info : [ errorMessage = ${e.message} ]")
            }

            val sheet = workbook.getSheetAt(0)

            sheet.forEachIndexed { index, row ->
                if (index == 0)
                    return@forEachIndexed

                if (row.getCell(0).stringCellValue == "")
                    return

                val schoolName = row.getCell(0).stringCellValue
                val clubName = row.getCell(1).stringCellValue
                val field = row.getCell(2).stringCellValue

                val school = schoolRepository.findByName(schoolName)
                    ?: throw SchoolNotFoundException("존재하지 않는 학교입니다. info : [ schoolName = $schoolName ]")

                if (clubRepository.existsByName(clubName)) {
                    throw AlreadyExistClubException("이미 존재하는 동아리입니다. info : [ clubName = $clubName ]")
                }

                val clubField = when (field) {
                    FUTURISTIC_TRANSPORTATION_EQUIPMENT -> Field.FUTURISTIC_TRANSPORTATION_EQUIPMENT
                    ENERGY -> Field.ENERGY
                    MEDICAL_HEALTHCARE -> Field.MEDICAL_HEALTHCARE
                    AI_CONVERGENCE -> Field.AI_CONVERGENCE
                    CULTURE -> Field.CULTURE
                    else -> throw InvalidFieldException("유효하지 않은 동아리 분야입니다. info : [ clubField = $field ]")
                }

                val club = Club(
                    school = school,
                    name = clubName,
                    field = clubField
                )
                clubRepository.save(club)
            }
        }
    }

    /**
     * 강의 리스트 엑셀을 업로드 하는 비지니스 로직입니다
     * @param file  강의 리스트 엑셀 업로드를 위한 MultipartFile
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun uploadLectureListExcel(file: MultipartFile) {
        file.inputStream.use {
            val workbook = try {
                WorkbookFactory.create(file.inputStream)
            } catch (e: IndexOutOfBoundsException) {
                throw InvalidCellTypeException("셀 서식을 텍스트로 변경해주세요.")
            }  catch (e: Exception) {
                throw InternalServerException("엑셀 파일 처리 중 문제가 발생했습니다. info : [ errorMessage = ${e.message} ]")
            }

            val sheet = workbook.getSheetAt(0)

            sheet.forEachIndexed { index, row ->
                if (index == 0 || index == 1)
                    return@forEachIndexed

                if (row.getCell(0) == null || row.getCell(0).stringCellValue == "")
                    return

                val name = row.getCell(0).stringCellValue
                val content = row.getCell(1).stringCellValue

                val instructorName = row.getCell(2).stringCellValue
                val instructorEmail = row.getCell(3).stringCellValue

                val instructor = userRepository.findByNameAndEmail(instructorName, instructorEmail)
                    ?: throw UserNotFoundException("존재하지 않는 강사입니다. info : [ name = $instructorName, email = $instructorEmail ]")

                val type = row.getCell(4).stringCellValue
                val credit = row.getCell(5).numericCellValue.toInt()
                val semester = row.getCell(6).stringCellValue

                val lectureSemester = when(semester) {
                    "1학년 2학기" -> Semester.FIRST_YEAR_FALL_SEMESTER
                    "2학년 1학기" -> Semester.SECOND_YEAR_SPRING_SEMESTER
                    "2학년 2학기" -> Semester.SECOND_YEAR_FALL_SEMESTER
                    "3학년 1학기" -> Semester.THIRD_YEAR_SPRING_SEMESTER
                    else -> Semester.NOT_APPLICABLE
                }

                val division = row.getCell(7).stringCellValue
                val line = row.getCell(8).stringCellValue
                val department = row.getCell(9).stringCellValue
                val maxRegisteredUser = row.getCell(10).numericCellValue.toInt()

                val startDate = row.getCell(11).stringCellValue.toLocalDateTime()
                val endDate = row.getCell(12).stringCellValue.toLocalDateTime()
                val essentialComplete = row.getCell(16).stringCellValue

                val lectureEssentialComplete = essentialComplete == "O"

                val lecture = Lecture(
                    id = UUID(0, 0),
                    name = name,
                    content = content,
                    instructor = instructorName,
                    user = instructor,
                    lectureType = type,
                    credit = credit,
                    semester = lectureSemester,
                    division = division,
                    line = line,
                    department = department,
                    maxRegisteredUser = maxRegisteredUser,
                    startDate = startDate,
                    endDate = endDate,
                    essentialComplete = lectureEssentialComplete
                )

                lectureRepository.save(lecture)

                val lectureDates = row.getCell(13).stringCellValue
                    .split(",").map {
                        val timeZone = it.split(" ", "~")
                        LectureDate(
                            id = UUID(0, 0),
                            lecture = lecture,
                            completeDate = timeZone[0].toLocalDate(),
                            startTime = timeZone[1].toLocalTime(),
                            endTime = timeZone[2].toLocalTime()
                        )
                    }

                lectureDateRepository.saveAll(lectureDates)

                val address = row.getCell(14).stringCellValue
                val addressDetails = row.getCell(15).stringCellValue

                val coordinate = kakaoUtil.getCoordinate(address)
                val lectureLocation = LectureLocation(
                    lectureId = lecture.id,
                    x = coordinate.first,
                    y = coordinate.second,
                    address = address,
                    details = addressDetails
                )

                lectureLocationRepository.save(lectureLocation)
            }
        }
    }

    private fun validateExcelStudentData(email: String, phoneNumber: String, password: String) {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        if (!email.matches(emailRegex))
            throw InvalidEmailException("유효하지 않은 이메일입니다. info : [ email = $email ]")

        val phoneRegex = "^010[0-9]{8}\$".toRegex()
        if (!phoneNumber.matches(phoneRegex))
            throw InvalidPhoneNumberException("유효하지 않은 휴대폰 번호입니다. info : [ phoneNumber = $phoneNumber ]")

        val passwordRegex = "^(?=.*[A-Za-z0-9])[A-Za-z0-9!@#\\\\\$%^&*]{8,24}\$".toRegex()
        if (!password.matches(passwordRegex))
            throw InvalidPasswordException("유효하지 않은 비밀번호입니다. info : [ password = $password ]")
    }

    private infix fun ClubRepository.findByName(clubName: String): Club =
        this.findByName(clubName) ?: throw ClubNotFoundException("존재하지 않는 동아리입니다. info : [ clubName = $clubName ]")


    companion object {
        const val FUTURISTIC_TRANSPORTATION_EQUIPMENT = "미래형 운송기기"
        const val ENERGY = "에너지 산업"
        const val MEDICAL_HEALTHCARE = "의료 헬스케어"
        const val AI_CONVERGENCE = "AI 융복합"
        const val CULTURE = "문화산업"
    }

    private fun String.toLocalDateTime(): LocalDateTime =
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

    private fun String.toLocalDate(): LocalDate =
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    private fun String.toLocalTime(): LocalTime =
        LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
}