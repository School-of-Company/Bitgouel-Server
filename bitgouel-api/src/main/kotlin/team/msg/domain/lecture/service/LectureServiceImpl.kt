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
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.LectureNotFoundException
import team.msg.domain.lecture.exception.NotAvailableSignUpDateException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.exception.UnSignedUpLectureException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllDepartmentsRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLinesRequest
import team.msg.domain.lecture.presentation.data.response.CompletedLecturesResponse
import team.msg.domain.lecture.presentation.data.response.DepartmentsResponse
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LectureResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LinesResponse
import team.msg.domain.lecture.repository.LectureDateRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.professor.exception.ProfessorNotFoundException
import team.msg.domain.professor.repository.ProfessorRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.teacher.exception.TeacherNotFoundException
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
    private val professorRepository: ProfessorRepository,
    private val userRepository: UserRepository,
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
            maxRegisteredUser = request.maxRegisteredUser
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
    override fun queryAllLectures(pageable: Pageable, queryAllLectureRequest: QueryAllLectureRequest): LecturesResponse {
        val lectureType = queryAllLectureRequest.lectureType

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
    override fun queryAllCompletedLecturesByStudent(studentId: UUID): CompletedLecturesResponse {
        if(!studentRepository.existsById(studentId))
            throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ id = $studentId ]")

        val completedLectures = registeredLectureRepository.findLecturesByStudentId(studentId)
            .mapNotNull { lecture ->
                lectureDateRepository.findByLatestLectureDate(lecture.id)
                    ?.let { latestLectureDate -> LectureResponse.of(lecture, latestLectureDate) }
            }

        val response = LectureResponse.completedOf(completedLectures)

        return response
    }

    @Transactional(readOnly = true,rollbackFor = [Exception::class])
    override fun lectureReceiptStatusExcel(response: HttpServletResponse) {
        val workBook = XSSFWorkbook()
        val headers = listOf(
            "연번" to 5,
            "구분" to 15,
            "계열" to 10,
            "학기" to 5,
            "대학" to 15,
            "학과" to 20 ,
            "교과명" to 30,
            "교육일정" to 25,
            "담당교수" to 10,
            "학교명" to 25,
            "동아리" to 10,
            "학년" to 5,
            "학생 성명" to 30,
            "담당 교사" to 10
        )

        val sheet = workBook.createSheet()

        val headerRow = sheet.createRow(0)

        val font = workBook.createFont()
        font.fontName = "Arial"
        font.fontHeightInPoints = 11

        val style = workBook.createCellStyle()
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.setFont(font)

        headers.forEachIndexed { idx, header ->
            headerRow.createCellWithOptions(idx, header.first, style, 20F)

            sheet.autoSizeColumn(idx)
            sheet.setColumnWidth(idx, sheet.getColumnWidth(idx) + (256 * header.second))
        }

        val lectures = lectureRepository.findAll()

        lectures.forEach { lecture ->
            val registeredLecture = registeredLectureRepository.findAllByLecture(lecture)

            registeredLecture.forEachIndexed { serialNumber, it ->
                val row = sheet.createRow(serialNumber + 1)

                val club = it.student.club

                val teacher = teacherRepository.findByClub(club)
                    ?: throw TeacherNotFoundException("해당 동아리의 선생님을 찾을 수 없습니다. info : [ clubId = ${club.id} ]")

                val professor = professorRepository.findByUser(lecture.user)
                    ?: throw ProfessorNotFoundException("해당 교수를 찾을 수 없습니다 : [ userId = ${lecture.user.id}]")

                val lectureDate = lectureDateRepository.findAllByLecture(lecture)

                listOf(
                    (serialNumber+1).toString(),
                    lecture.division,
                    lecture.line,
                    lecture.semester.yearAndSemester,
                    professor.university,
                    lecture.department,
                    lecture.name,
                    lectureDate.joinToString("\n") { "${it.completeDate}, ${it.startTime} ~ ${it.endTime}" },
                    lecture.instructor,
                    club.school.highSchool.schoolName,
                    club.name,
                    it.student.grade.toString(),
                    it.student.user!!.name,
                    teacher.user!!.name
                ).forEachIndexed { idx, data ->
                    row.createCellWithOptions(idx, data, style, 40F)
                    row.height = -1
                }
            }
        }

        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.setHeader("Content-Disposition", "attachment;lecture_result.xlsx")

        workBook.use {
            it.write(response.outputStream)
        }
    }

    fun XSSFRow.createCellWithOptions(idx: Int, data: String,style: XSSFCellStyle, height: Float) {
        val cell = this.createCell(idx)
        cell.setCellValue(data)
        cell.cellStyle = style
        this.heightInPoints = height
    }
    private infix fun LectureRepository.findById(id: UUID): Lecture = this.findByIdOrNull(id)
        ?: throw LectureNotFoundException("존재하지 않는 강의입니다. info : [ lectureId = $id ]")

    private infix fun StudentRepository.findByUser(user: User): Student = this.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun UserRepository.findById(id: UUID): User = this.findByIdOrNull(id)
        ?: throw UserNotFoundException("유저를 찾을 수 없습니다. info : [ userId = $id ]")
}