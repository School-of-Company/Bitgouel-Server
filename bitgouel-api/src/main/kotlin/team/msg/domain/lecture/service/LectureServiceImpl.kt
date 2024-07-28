package team.msg.domain.lecture.service

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
import team.msg.domain.lecture.exception.*
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.LectureDate
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.*
import team.msg.domain.lecture.presentation.data.response.*
import team.msg.domain.lecture.repository.LectureDateRepository
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
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
import javax.servlet.http.HttpServletResponse
import team.msg.common.util.KakaoUtil
import team.msg.domain.lecture.enums.CompleteStatus
import team.msg.domain.lecture.enums.Semester
import team.msg.domain.lecture.model.LectureLocation
import team.msg.domain.lecture.repository.LectureLocationRepository
import team.msg.domain.student.exception.InvalidStudentGradeException

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
    private val lectureLocationRepository: LectureLocationRepository,
    private val userUtil: UserUtil,
    private val kakaoUtil: KakaoUtil
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
            id = UUID(0, 0),
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
                id = UUID(0, 0),
                lecture = savedLecture,
                completeDate = it.completeDate,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }

        lectureDateRepository.saveAll(lectureDates)

        val coordinate = kakaoUtil.getCoordinate(request.address)
        val lectureLocation = LectureLocation(
            lectureId = savedLecture.id,
            x = coordinate.first,
            y = coordinate.second,
            address = request.address,
            details = request.locationDetails
        )

        lectureLocationRepository.save(lectureLocation)
    }

    /**
     * 강의 정보 수정을 처리하는 비지니스 로직입니다.
     * @param 수정할 강의 id, 수정할 강의의 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateLecture(id: UUID, request: UpdateLectureRequest) {
        val lecture = lectureRepository findById id
        val instructorUser = userRepository findById request.userId

        val currentUser = userUtil.queryCurrentUser()

        if(currentUser.authority != Authority.ROLE_ADMIN && lecture.user?.id != currentUser.id)
            throw ForbiddenLectureException("강의를 수정할 수 있는 권한이 없습니다. info : [ userId = ${currentUser.id} ]")

        val credit = if(request.lectureType != "상호학점인정교육과정") 0 else request.credit
        val semester = if(request.lectureType == "대학탐방프로그램") Semester.NOT_APPLICABLE else request.semester

        val updatedLecture = Lecture(
            id = id,
            user = instructorUser,
            name = request.name,
            semester = semester,
            division = request.division,
            department = request.department,
            line = request.line,
            startDate = request.startDate,
            endDate = request.endDate,
            content = request.content,
            lectureType = request.lectureType,
            credit = credit,
            instructor = instructorUser.name,
            maxRegisteredUser = request.maxRegisteredUser,
            essentialComplete = request.essentialComplete
        )

        val savedLecture = lectureRepository.save(updatedLecture)

        lectureDateRepository.deleteAllByLectureId(id)

        val lectureDates = request.lectureDates.map {
            LectureDate(
                id = UUID(0, 0),
                lecture = savedLecture,
                completeDate = it.completeDate,
                startTime = it.startTime,
                endTime = it.endTime
            )
        }

        lectureDateRepository.saveAll(lectureDates)

        val lectureLocation = lectureLocationRepository.findByLectureId(lecture.id)

        when {
            lectureLocation.address != request.address -> {
                val coordinate = kakaoUtil.getCoordinate(request.address)
                val updatedLectureLocation = LectureLocation(
                    id = lectureLocation.id,
                    lectureId = savedLecture.id,
                    x = coordinate.first,
                    y = coordinate.second,
                    address = request.address,
                    details = request.locationDetails
                )

                lectureLocationRepository.save(updatedLectureLocation)
            }
            lectureLocation.details != request.locationDetails -> {
                val updatedLectureLocation = LectureLocation(
                    id = lectureLocation.id,
                    lectureId = savedLecture.id,
                    x = lectureLocation.x,
                    y = lectureLocation.y,
                    address = request.address,
                    details = request.locationDetails
                )

                lectureLocationRepository.save(updatedLectureLocation)
            }
        }
    }
    /**
     * 강의를 논리적으로 삭제하는 비지니스 로직입니다.
     * @param 논리적으로 삭제할 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteLecture(id: UUID) {
        val lecture = lectureRepository findById id

        val currentUser = userUtil.queryCurrentUser()

        if(currentUser.authority != Authority.ROLE_ADMIN && lecture.user?.id != currentUser.id)
            throw ForbiddenLectureException("강의를 삭제할 수 있는 권한이 없습니다. info : [ userId = ${currentUser.id} ]")

        lectureRepository.delete(lecture)
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
                LectureResponse.of(it.lecture, it.registeredLectureCount.toInt())
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

        val lectureLocation = lectureLocationRepository.findByLectureId(id)

        val response = LectureResponse.detailOf(
            lecture = lecture,
            headCount = registeredLectureCount,
            isRegistered = isRegistered,
            lectureDates = lectureDates,
            lectureLocation = lectureLocation
        )

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
            id = UUID(0, 0),
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
            subscriptionGrade = student.subscriptionGrade,
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
            subscriptionGrade = student.subscriptionGrade,
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
                val lecture = it.lecture
                val isComplete = it.registeredLecture.completeStatus

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
        }.map { LectureResponse.of(it.student, it.registeredLecture.idComplete()) }

        val response = LectureResponse.signedUpOf(students)

        return response
    }

    /**
     * 강의에 신청한 학생의 상세정보를 조회하는 비지니스 로직입니다.
     *
     * 기업 강사, 유관기관 강사, 대학 교수 -> 자기 강의만 조회할 수 있음 (다른 강사의 강의 학생 조회 시 예외)
     * 뽀짝 선생님, 취업 동아리 선생님 -> 담당 동아리 소속 학생만 조회
     * 어드민 -> 제한 없음
     *
     * @param 조회할 강의 id, 학생 id
     * @return 조회한 학생 정보를 담은 dto
     */
    @Transactional(readOnly = true)
    override fun querySignedUpStudentDetails(id: UUID, studentId: UUID): SignedUpStudentDetailsResponse {
        val student = studentRepository findById studentId
        val registeredLecture = registeredLectureRepository.findByLectureIdAndStudentId(id, studentId)
            ?: throw UnSignedUpLectureException("학생의 강의 신청 기록을 찾을 수 없습니다. info : [ lectureId = $id, studentId = ${student.id} ]")
        val currentCompletedDate = lectureDateRepository.findByCurrentCompletedDate(id)

        val response = LectureResponse.signedUpDetailOf(student, registeredLecture.completeStatus, currentCompletedDate)

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
    override fun updateLectureCompleteStatus(id: UUID, studentIds: List<UUID>) {
        val user = userUtil.queryCurrentUser()
        val students = studentRepository.findByIdIn(studentIds)
        val lecture = lectureRepository findById id

        when(user.authority) {
            Authority.ROLE_TEACHER -> {
                val teacher = teacherRepository findByUser user

                if(students.any { teacher.club != it.club })
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")

            }
            Authority.ROLE_BBOZZAK -> {
                val bbozzak = bbozzakRepository findByUser user

                if(students.any { bbozzak.club != it.club })
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
            Authority.ROLE_ADMIN -> { }
            else -> {
                if (lecture.user != user)
                    throw ForbiddenSignedUpLectureException("학생의 이수 여부를 변경할 권한이 없습니다. info : [ userId = ${user.id} ]")
            }
        }

        val updatedRegisteredLectures = students.map { student ->
            val registeredLecture = registeredLectureRepository.findByStudentAndLecture(student, lecture)
                ?: throw UnSignedUpLectureException("학생의 강의 신청 기록을 찾을 수 없습니다. info : [ lectureId = $id, studentId = ${student.id} ]")

            val completeStatus = when(student.grade) {
                1 -> CompleteStatus.COMPLETED_IN_1RD
                2 -> CompleteStatus.COMPLETED_IN_2RD
                3 -> CompleteStatus.COMPLETED_IN_3RD
                else -> throw InvalidStudentGradeException("유효하지 않은 학년입니다. info : [ grade = ${student.grade} ]")
            }

            RegisteredLecture(
                id = registeredLecture.id,
                student = registeredLecture.student,
                lecture = registeredLecture.lecture,
                completeStatus = completeStatus
            )
        }

        registeredLectureRepository.saveAll(updatedRegisteredLectures)
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    override fun lectureReceiptStatusExcel(response: HttpServletResponse) {
        val workBook = XSSFWorkbook()

        val font = workBook.createFont()
        font.fontName = "Arial"
        font.fontHeightInPoints = 11

        val style = workBook.createCellStyle()
        style.alignment = HorizontalAlignment.CENTER
        style.verticalAlignment = VerticalAlignment.CENTER
        style.setFont(font)

        // 엑셀 삽입할 헤더
        val headers = listOf(
            "연번" to 10,
            "구분" to 30,
            "계열" to 20,
            "학기" to 10,
            "대학" to 30,
            "학과" to 20,
            "교과명" to 40,
            "교육일정" to 50,
            "교육내용" to 50,
            "교육 장소" to 30,
            "담당 교수" to 20,
            "연락처" to 30,
            "학교명" to 30,
            "동아리" to 30,
            "학년" to 10,
            "학생 성명" to 20,
            "담당교사" to 20,
            "담당교사 연락처" to 30,
            "이수 상태" to 20
        )

        val schools = schoolRepository.findAll()

        schools.forEach { highSchool ->
            val sheet = workBook.createSheet(highSchool.name)

            val headerRow = sheet.createRow(0)

            headers.forEachIndexed { idx,header ->
                headerRow.createCellWithOptions(idx, header.first, style, 30F)

                sheet.autoSizeColumn(idx)
                sheet.setColumnWidth(idx, sheet.getColumnWidth(idx) + (256 * header.second))
            }

            val clubs = clubRepository.findAllBySchool(highSchool)

            clubs.map { club ->
                val teacher = teacherRepository.findByClub(club)
                    ?: throw TeacherNotFoundException("취업 동아리 선생님을 찾을 수 없습니다. info : [ clubId = ${club.id} ]")

                val students = studentRepository.findAllByClub(club)

                students.forEachIndexed { idx, student ->
                    val registeredLectures = registeredLectureRepository.findAllByStudent(student)

                    registeredLectures.map { registeredLecture ->
                        val lecture = registeredLecture.lecture

                        val lectureDates = lectureDateRepository.findAllByLecture(lecture).sortedBy { it.completeDate }

                        val startTime = "${lectureDates.first().startTime.hour}:${lectureDates.first().startTime.minute}"

                        val endTime = "${lectureDates.first().endTime.hour}:${lectureDates.first().endTime.minute}"

                        val location = lectureLocationRepository.findByLectureId(lecture.id)

                        val row = sheet.createRow(idx+1)

                        listOf(
                            (idx+1).toString(),
                            lecture.division,
                            lecture.line,
                            lecture.semester.yearAndSemester,
                            location.address,
                            lecture.department,
                            lecture.name,
                            "${lectureDates.first().completeDate} ~ ${lectureDates.last().completeDate} $startTime ~ $endTime",
                            lecture.content,
                            location.details,
                            lecture.instructor,
                            lecture.user!!.phoneNumber,
                            highSchool.name,
                            club.name,
                            student.grade.toString(),
                            student.user!!.name,
                            teacher.user!!.name,
                            teacher.user!!.phoneNumber,
                            registeredLecture.completeStatus.name
                        ).forEachIndexed { cellIdx, parameter ->
                            val cell = row.createCell(cellIdx)
                            cell.setCellValue(parameter)
                            cell.cellStyle = style
                        }
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