package team.msg.domain.admin.service

import javax.servlet.http.HttpServletResponse
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.hssf.usermodel.HSSFFont
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFHyperlink
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import team.msg.common.enums.ApproveStatus
import team.msg.common.enums.Field
import team.msg.common.util.StudentUtil
import team.msg.common.util.UserUtil
import team.msg.domain.admin.exception.InvalidCellTypeException
import team.msg.domain.admin.presentation.data.request.QueryUsersRequest
import team.msg.domain.club.exception.AlreadyExistClubException
import team.msg.domain.club.exception.ClubNotFoundException
import team.msg.domain.club.exception.InvalidFieldException
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
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
import team.msg.global.exception.InternalServerException
import java.util.*

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository,
    private val userUtil: UserUtil,
    private val studentUtil: StudentUtil,
    private val clubRepository: ClubRepository,
    private val studentRepository: StudentRepository,
    private val schoolRepository: SchoolRepository
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

    @Transactional(readOnly = true)
    override fun downloadClubStatusExcel(response: HttpServletResponse) {
        val workBook = XSSFWorkbook()

        val defaultFont = workBook.createFont().apply {
            fontName = "Arial"
            fontHeightInPoints = 11
        }

        val defaultStyle = workBook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            setFont(defaultFont)
        }

        val headerStyle = workBook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            setFont(defaultFont)
            fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
        }

        val hyperlinkFont = workBook.createFont().apply {
            fontName = "Arial"
            fontHeightInPoints = 11
            color = IndexedColors.BLUE.index
            underline = XSSFFont.U_DOUBLE
        }

        val hyperlinkStyle = workBook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            setFont(hyperlinkFont)
        }

        val headers = listOf(
            "시트 이동" to 5,
            "연번" to 5,
            "핵심분야" to 10,
            "계열" to 10,
            "학교" to 20,
            "동아리명" to 20,
            "합계" to 5,
            "1학년" to 5,
            "2학년" to 5,
            "3학년" to 5
        )

        val clubs = clubRepository.findAll()

        val sheet = workBook.createSheet("취업 동아리 명단")

        val header1stRow = sheet.createRow(0)
        header1stRow.createCellWithOptions(7, "현황", headerStyle)
        sheet.autoSizeColumn(7)
        sheet.setColumnWidth(7, sheet.getColumnWidth(7) + (256 * 20))

        val header2ndRow = sheet.createRow(1)
        headers.forEachIndexed { idx, header ->
            header2ndRow.createCellWithOptions(idx, header.first, headerStyle)

            sheet.autoSizeColumn(idx)
            sheet.setColumnWidth(idx, sheet.getColumnWidth(idx) + (256 * header.second))
        }

        clubs.forEachIndexed { idx, club ->
            val row = sheet.createRow(idx + 2)

            val creationHelper = workBook.creationHelper
            val hyperlink = creationHelper.createHyperlink(HyperlinkType.URL)
            hyperlink.address = "https://bitgouel-admin.vercel.app/main/club/detail/${club.id}"
            row.createCellWithHyperLink(0, "명단", hyperlinkStyle, hyperlink)

            row.createCellWithOptions(1, (idx + 1).toString(), defaultStyle)

            val clubField = when (club.field) {
                Field.FUTURISTIC_TRANSPORTATION_EQUIPMENT -> FUTURISTIC_TRANSPORTATION_EQUIPMENT
                Field.ENERGY -> ENERGY
                Field.MEDICAL_HEALTHCARE -> MEDICAL_HEALTHCARE
                Field.AI_CONVERGENCE -> AI_CONVERGENCE
                Field.CULTURE -> CULTURE
                else -> throw InvalidFieldException("유효하지 않은 동아리 분야입니다. info : [ clubField = ${club.field} ]")
            }
            row.createCellWithOptions(2, clubField, defaultStyle)

            row.createCellWithOptions(3, club.name, defaultStyle)
            row.createCellWithOptions(4, club.school.name, defaultStyle)
            row.createCellWithOptions(5, club.name, defaultStyle)

            val studentCount = studentRepository.countByClub(club)
            row.createCellWithOptions(6, studentCount.toString(), defaultStyle)

            val grade1stStudentCount = studentRepository.countByClubAndGrade(club, 1)
            row.createCellWithOptions(7, grade1stStudentCount.toString(), defaultStyle)

            val grade2ndStudentCount = studentRepository.countByClubAndGrade(club, 2)
            row.createCellWithOptions(8, grade2ndStudentCount.toString(), defaultStyle)

            val grade3ndStudentCount = studentRepository.countByClubAndGrade(club, 3)
            row.createCellWithOptions(9, grade3ndStudentCount.toString(), defaultStyle)
        }

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader("Content-Disposition", "attachment;club.xlsx")

        workBook.use {
            it.write(response.outputStream)
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

    fun XSSFRow.createCellWithOptions(idx: Int, data: String, style: XSSFCellStyle) {
        val cell = this.createCell(idx)
        cell.setCellValue(data)
        cell.cellStyle = style
        this.heightInPoints = 30F
    }

    fun XSSFRow.createCellWithHyperLink(idx: Int, data: String, style: XSSFCellStyle, hyperlink: XSSFHyperlink) {
        val cell = this.createCell(idx)
        cell.setCellValue(data)
        cell.cellStyle = style
        cell.hyperlink = hyperlink
        this.heightInPoints = 30F
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
}