package team.msg.domain.lecture.service

import javax.servlet.http.HttpServletResponse
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.annotation.DistributedLock
import team.msg.common.util.UserUtil
import team.msg.domain.bbozzak.exception.BbozzakNotFoundException
import team.msg.domain.bbozzak.model.Bbozzak
import team.msg.domain.bbozzak.repository.BbozzakRepository
import team.msg.domain.club.model.Club
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.ForbiddenSignedUpLectureException
import team.msg.domain.lecture.exception.LectureNotFoundException
import team.msg.domain.lecture.exception.NotAvailableSignUpDateException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.exception.UnSignedUpLectureException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDivisionsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.response.DepartmentsResponse
import team.msg.domain.lecture.presentation.data.response.DivisionsResponse
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LectureResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LinesResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpLecturesResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpStudentsResponse
import team.msg.domain.lecture.repository.LectureDateRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.exception.SchoolNotFoundException
import team.msg.domain.school.repository.SchoolRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
import team.msg.domain.teacher.model.Teacher
import team.msg.domain.teacher.repository.TeacherRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class LectureServiceImpl(
    private val lectureRepository: LectureRepository,
    private val lectureDateRepository: LectureDateRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val bbozzakRepository: BbozzakRepository,
    private val userRepository: UserRepository,
    private val clubRepository: ClubRepository,
    private val schoolRepository: SchoolRepository,
    private val userUtil: UserUtil
) : LectureService {

    /**
     * 강의 개설을 처리하는 비지니스 로직입니다.
     * @param 생성할 강의의 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createLecture(request: CreateLectureRequest) {
        val user = userRepository findById request.userId

        val credit = if(request.lectureType != "상호학점인정교육과정") 0 else request.credit

        val lecture = Lecture(
            id = UUID.randomUUID(),
            user = user,
            name = request.name,
            semester = request.semester,
            division = request.division,
            department = request.department,
            line = request.line,
            startDate = request.startDate,
            endDate = request.endDate,
            content = request.content,
            lectureType = request.lectureType,
            credit = credit,
            instructor = user.name,
            maxRegisteredUser = request.maxRegisteredUser,
            essentialComplete = request.essentialComplete
        )

        val savedLecture = lectureRepository.save(lecture)

        val lectureDates = request.lectureDates.map {
            LectureDate(
                id = UUID.randomUUID(),
                lecture = savedLecture,
                completeDate = it.completeDate,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }

        lectureDateRepository.saveAll(lectureDates)
    }

    /**
     * 강의를 조회하는 비지니스 로직입니다.
     * @param 조회할 강의의 승인 상태를 담은 request dto와 pageable dto
     * @return 조회한 강의의 정보를 담은 list dto
     */
    @Transactional(readOnly = true)
    override fun queryAllLectures(pageable: Pageable, request: QueryAllLectureRequest): LecturesResponse {
        val lectureType = request.lectureType

        val lectures = lectureRepository.findAllByLectureType(pageable, lectureType)

        val response = LecturesResponse(
            lectures.map {
                val registeredLectureCount = registeredLectureRepository.countByLecture(it)
                LectureResponse.of(it, registeredLectureCount)
            }
        )

        return response
    }

    /**
     * 강의에 대한 상세정보를 조회하는 비지니스 로직입니다.
     * @param 상세 조회할 강의의 id
     * @return 강의의 상세조회 정보를 담은 detail dto
     */
    @Transactional(readOnly = true)
    override fun queryLectureDetails(id: UUID): LectureDetailsResponse {
        val user = userUtil.queryCurrentUser()

        val lecture = lectureRepository findById id

        val lectureDates = lectureDateRepository.findAllByLecture(lecture)

        val registeredLectureCount = registeredLectureRepository.countByLecture(lecture)

        val isRegistered = if(user.authority == Authority.ROLE_STUDENT) {
            val student = studentRepository findByUser user
            registeredLectureRepository.existsOne(student.id, lecture.id)
        } else false

        val response = LectureResponse.detailOf(lecture, registeredLectureCount, isRegistered, lectureDates)

        return response
    }

    /**
     * 서버에 저장된 구분에 따른 계열 리스트를 조회하는 비지니스 로직입니다.
     * 지정된 구분, 키워드를 통해 필터링 합니다.
     * @param 구분, 키워드
     * @return 계열 리스트 response
     */
    @Transactional(readOnly = true)
    override fun queryAllLines(request: QueryAllLinesRequest): LinesResponse {
        val lines = lectureRepository.findAllLineByDivision(request.division, request.keyword)
        val response = LectureResponse.lineOf(lines)

        return response
    }

    /**
     * 서버에 저장된 학과 리스트를 조회하는 비지니스 로직입니다.
     * 키워드를 통해 필터링 합니다.
     * @param 키워드
     * @return 학과 리스트 response
     */
    @Transactional(readOnly = true)
    override fun queryAllDepartments(request: QueryAllDepartmentsRequest): DepartmentsResponse {
        val departments = lectureRepository.findAllDepartment(request.keyword)
        val response = LectureResponse.departmentOf(departments)

        return response
    }

    /**
     * 서버에 저장된 구분 리스트를 조회하는 비지니스 로직입니다.
     * 키워드를 통해 필터링 합니다.
     * @param 키워드
     * @return 구분 리스트 response
     */
    @Transactional(readOnly = true)
    override fun queryAllDivisions(request: QueryAllDivisionsRequest): DivisionsResponse {
        val divisions = lectureRepository.findAllDivisions(request.keyword)
        val response = LectureResponse.divisionOf(divisions)

        return response
    }

    /**
     * 강의에 대해 수강신청하는 비지니스 로직입니다.
     * @param 수강신청을 하기 위한 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    @DistributedLock(
        key = "#id",
        leaseTime = 10,
        waitTime = 5,
        timeUnit = TimeUnit.SECONDS)
    override fun signUpLecture(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val lecture = lectureRepository findById id

        if(lecture.getLectureStatus() == LectureStatus.CLOSED)
            throw NotAvailableSignUpDateException("수강신청이 가능한 시간이 아닙니다. info : [ lectureId = ${lecture.id}, currentTime = ${LocalDateTime.now()} ]")

        if(registeredLectureRepository.existsOne(student.id, lecture.id))
            throw AlreadySignedUpLectureException("이미 신청한 강의입니다. info : [ lectureId = ${lecture.id}, studentId = ${student.id} ]")

        val registeredLectureCount = registeredLectureRepository.countByLecture(lecture)

        if(lecture.maxRegisteredUser <= registeredLectureCount)
            throw OverMaxRegisteredUserException("수강 인원이 가득 찼습니다. info : [ maxRegisteredUser = ${lecture.maxRegisteredUser}, currentSignUpLectureStudent = $registeredLectureCount ]")

        val registeredLecture = RegisteredLecture(
            id = UUID.randomUUID(),
            student = student,
            lecture = lecture
        )

        registeredLectureRepository.save(registeredLecture)

        val updateCreditStudent = Student(
            id = student.id,
            user = student.user,
            club = student.club,
            grade = student.grade,
            classRoom = student.classRoom,
            number = student.number,
            cohort = student.cohort,
            credit = student.credit + lecture.credit,
            studentRole = student.studentRole
        )

        studentRepository.save(updateCreditStudent)
    }

    /**
     * 강의에 대해 수강신청 취소하는 비지니스 로직입니다.
     * @param 수강신청을 취소하기 위한 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun cancelSignUpLecture(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val lecture = lectureRepository findById id

        if(lecture.getLectureStatus() == LectureStatus.CLOSED)
            throw NotAvailableSignUpDateException("수강신청 취소가 가능한 시간이 아닙니다. info : [ lectureId = ${lecture.id}, currentTime = ${LocalDateTime.now()} ]")

        val registeredLecture = registeredLectureRepository.findByStudentAndLecture(student, lecture)
            ?: throw UnSignedUpLectureException("신청하지 않은 강의입니다. info : [ lectureId = ${lecture.id}, studentId = ${student.id} ]")

        registeredLectureRepository.delete(registeredLecture)

        val updateCreditStudent = Student(
            id = student.id,
            user = student.user,
            club = student.club,
            grade = student.grade,
            classRoom = student.classRoom,
            number = student.number,
            cohort = student.cohort,
            credit = student.credit - lecture.credit,
            studentRole = student.studentRole
        )

        studentRepository.save(updateCreditStudent)
    }

    /**
     * 강사를 키워드로 조회하는 비지니스 로직입니다.
     *
     * @param 조회할 강사 이름과 소속 keyword
     * @return 조회한 강사 유저와 소속 정보를 담은 list dto
     */
    @Transactional(readOnly = true)
    override fun queryInstructors(keyword: String): InstructorsResponse {
        val instructors = userRepository.queryInstructorsAndOrganization(keyword)

        val response = InstructorsResponse(
            instructors.map {
                LectureResponse.instructorOf(it.first, it.second)
            }
        )

        return response
    }

    /**
     * 유저가 신청한 강의 내역을 조회하는 비지니스 로직입니다.
     *
     * @param 조회할 유저 id
     * @return 조회한 강의 정보를 담은 list dto
     */
    @Transactional(readOnly = true)
    override fun queryAllSignedUpLectures(studentId: UUID): SignedUpLecturesResponse {
        val user = userUtil.queryCurrentUser()

        if(user.authority == Authority.ROLE_TEACHER) {
            val teacher = teacherRepository findByUser user
            val student = studentRepository findById studentId

            if(teacher.club != student.club)
                throw ForbiddenSignedUpLectureException("학생의 수강 이력을 볼 권한이 없습니다. info : [ teacherId = ${teacher.id} ]")
        } else if(user.authority == Authority.ROLE_STUDENT) {
            val student = studentRepository findById studentId

            if(student.user != user)
                throw ForbiddenSignedUpLectureException("자신의 수강 이력만 볼 수 있습니다. info : [ userId = ${user.id} ]")
        }

        val signedUpLectures = registeredLectureRepository.findLecturesAndIsCompleteByStudentId(studentId)
            .map {
                val lecture = it.first
                val isComplete = it.second

                lectureDateRepository.findByCurrentCompletedDate(lecture.id)
                    .let { currentCompletedDate -> LectureResponse.of(lecture, isComplete, currentCompletedDate) }
            }

        val response = LectureResponse.signedUpOf(signedUpLectures)

        return response
    }

    /**
     * 강의에 신청한 학생 리스트를 조회하는 비지니스 로직입니다.
     *
     * 기업 강사, 유관기관 강사, 대학 교수 -> 자기 강의만 조회할 수 있음 (다른 강사의 강의 학생 리스트 조회 시 예외)
     * 뽀짝 선생님, 취업 동아리 선생님 -> 담당 동아리 소속 학생만 조회
     * 어드민 -> 제한 없음
     *
     * @param 조회할 강의 id
     * @return 조회한 학생 정보를 담은 list dto
     */
    @Transactional(readOnly = true)
    override fun queryAllSignedUpStudents(id: UUID): SignedUpStudentsResponse {
        val user = userUtil.queryCurrentUser()

        val students = when(user.authority){
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user
                registeredLectureRepository.findSignedUpStudentsByLectureIdAndClubId(id, teacher.club.id)
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user
                registeredLectureRepository.findSignedUpStudentsByLectureIdAndClubId(id, bbozzak.club.id)
            }
            Authority.ROLE_ADMIN -> registeredLectureRepository.findSignedUpStudentsByLectureId(id)
            else -> {
                val lecture = lectureRepository findById id
                if(lecture.user != user)
                    throw ForbiddenSignedUpLectureException("학생의 수강 이력을 볼 권한이 없습니다. info : [ userId = ${user.id} ]")
                registeredLectureRepository.findSignedUpStudentsByLectureId(id)
            }
        }.map { LectureResponse.of(it.first, it.second) }

        val response = LectureResponse.signedUpOf(students)

        return response
    }

    /**
     * 강의 수강 여부를 업데이트하는 비지니스 로직입니다.
     *
     * 기업 강사, 유관기관 강사, 대학 교수 -> 자기 강의 학생들만 변경할 수 있음 (다른 강사의 강의 학생 수강 여부 변경 시 예외)
     * 뽀짝 선생님, 취업 동아리 선생님 -> 담당 동아리 소속 학생만 변경
     * 어드민 -> 제한 없음
     *
     * @param 이수 상태를 변경할 강의 id와 학생 id, 변결될 강의 여부
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateLectureCompleteStatus(id: UUID, studentId: UUID, isComplete: Boolean) {
        val user = userUtil.queryCurrentUser()

        when(user.authority) {
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user
                val student = studentRepository findById studentId

                if(teacher.club != student.club)
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user
                val student = studentRepository findById studentId

                if(bbozzak.club != student.club)
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            Authority.ROLE_ADMIN -> { }
            else -> {
                val lecture = lectureRepository findById id
                if (lecture.user != user)
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
        }

        val registeredLecture = registeredLectureRepository.findByLectureIdAndStudentId(id, studentId)
            ?: throw UnSignedUpLectureException("학생의 강의 신청 기록을 찾을 수 없습니다. info : [ lectureId = $id, studentId = $studentId ]")
        
        val updatedRegisteredLecture = RegisteredLecture(
            id = registeredLecture.id,
            student = registeredLecture.student,
            lecture = registeredLecture.lecture,
            isComplete = isComplete
        )

        registeredLectureRepository.save(updatedRegisteredLecture)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    override fun lectureReceiptStatusExcel(response: HttpServletResponse) {
        val workBook = XSSFWorkbook()

        val cellStyle = workBook.createCellStyle()
        cellStyle.alignment = HorizontalAlignment.CENTER
        cellStyle.verticalAlignment = VerticalAlignment.CENTER

        // 엑셀 삽입할 헤더
        val headers = listOf(
            "연번" to 10,
            "학교" to 20,
            "동아리명" to 20,
            "학과명" to 20,
            "반" to 5,
            "이름" to 10,
            "휴대폰 번호(학생)" to 20,
            "담당교사명" to 20,
            "휴대폰번호(담당교사)" to 20,
            "이메일(담당교사)" to 20,
            "강의명" to 30,
            "학기" to 20,
            "강의 구분" to 30,
            "강의 과" to 20,
            "강의 계열" to 30,
            "강의 유형" to 30,
            "담당 강사" to 20,
            "학점" to 20,
            "필수강의 여부" to 20,
            "강의 이수 완료일" to 30
        )

        val font = workBook.createFont()
        font.fontName = "Arial"
        font.fontHeightInPoints = 11

        val style = workBook.createCellStyle()
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.setFont(font)

        HighSchool.values().forEach { highSchool ->
            // 엑셀 시트 생성
            val sheet = workBook.createSheet(highSchool.schoolName)

            // 열 생성
            val headerRow = sheet.createRow(0)

            headers.forEachIndexed { idx, header ->
                headerRow.createCellWithOptions(idx, header.first, style, 20F)

                sheet.autoSizeColumn(idx)
                sheet.setColumnWidth(idx,sheet.getColumnWidth(idx) + (256 * header.second))
            }

            val school = schoolRepository.findByHighSchool(highSchool)
                ?: throw SchoolNotFoundException("해당하는 학교를 찾을 수 없습니다. info : [ school = $highSchool]")

            val clubs = clubRepository.findAllBySchool(school)

            val students = clubs.map { club ->
                studentRepository.findAllByClub(club)
            }.flatten()

            val registeredLecture = students.map { student ->
                val registeredLecture = registeredLectureRepository.findAllByStudent(student)

                student to registeredLecture
            }

            registeredLecture.forEach { studentAndRegisteredLecture ->
                val teacher  = teacherRepository findByClub studentAndRegisteredLecture.first.club

                studentAndRegisteredLecture.second.forEachIndexed { idx, registeredLecture ->
                    val lecture = registeredLecture.lecture

                    val row = sheet.createRow(idx+1)

                    listOf(
                        (idx+1).toString(),
                        school.highSchool.schoolName,
                        studentAndRegisteredLecture.first.club.name,
                        "",
                        studentAndRegisteredLecture.first.classRoom.toString(),
                        studentAndRegisteredLecture.first.user!!.name,
                        studentAndRegisteredLecture.first.user!!.phoneNumber,
                        teacher.user!!.name,
                        teacher.user!!.phoneNumber,
                        teacher.user!!.email,
                        lecture.name,
                        lecture.semester.yearAndSemester,
                        lecture.division,
                        lecture.department,
                        lecture.line,
                        lecture.lectureType,
                        lecture.instructor,
                        lecture.credit.toString(),
                        if(lecture.essentialComplete) "O" else "X"
                    ).forEachIndexed {
                        cellIdx, parameter ->
                        val cell = row.createCell(cellIdx)
                        cell.setCellValue(parameter)
                        cell.cellStyle = cellStyle
                    }
                }
            }
        }

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader("Content-Disposition", "attachment;lecture_result.xlsx")

        workBook.use {
            it.write(response.outputStream)
        }
    }

    fun XSSFRow.createCellWithOptions(idx: Int, data: String, style: XSSFCellStyle, height: Float) {
        val cell = this.createCell(idx)
        cell.setCellValue(data)
        cell.cellStyle = style
        this.heightInPoints = height
    }

    private infix fun LectureRepository.findById(id: UUID): Lecture = this.findByIdOrNull(id)
        ?: throw LectureNotFoundException("존재하지 않는 강의입니다. info : [ lectureId = $id ]")

    private infix fun TeacherRepository.findByUser(user: User): Teacher = this.findByUser(user)
        ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun BbozzakRepository.findByUser(user: User): Bbozzak = this.findByUser(user)
        ?: throw BbozzakNotFoundException("뽀짝 선생님을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun StudentRepository.findByUser(user: User): Student = this.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun StudentRepository.findById(id: UUID): Student = this.findByIdOrNull(id)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = $id ]")

    private infix fun UserRepository.findById(id: UUID): User = this.findByIdOrNull(id)
        ?: throw UserNotFoundException("유저를 찾을 수 없습니다. info : [ userId = $id ]")

    private infix fun TeacherRepository.findByClub(club: Club): Teacher = this.findByClub(club)
        ?: throw TeacherNotFoundException("해당 동아리의 취업 동아리 선생님을 찾을 수 없습니다. info : [ club = ${club.name} ]")
}