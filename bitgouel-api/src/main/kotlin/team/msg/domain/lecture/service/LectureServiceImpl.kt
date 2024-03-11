package team.msg.domain.lecture.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.util.UserUtil
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.enums.LectureType
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.LectureNotFoundException
import team.msg.domain.lecture.exception.NotAvailableSignUpDateException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.exception.UnSignedUpLectureException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.presentation.data.request.QueryAllLectureRequest
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LectureResponse
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.model.Student
import team.msg.domain.student.repository.StudentRepository
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.exception.UserNotFoundException
import team.msg.domain.user.model.User
import team.msg.domain.user.repository.UserRepository
import java.time.LocalDateTime
import java.util.*

@Service
class LectureServiceImpl(
    private val lectureRepository: LectureRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val studentRepository: StudentRepository,
    private val userRepository: UserRepository,
    private val userUtil: UserUtil
) : LectureService{

    /**
     * 강의 개설을 처리하는 비지니스 로직입니다.
     * @param 생성할 강의의 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createLecture(request: CreateLectureRequest) {
        val user = userRepository findById request.userId

        val credit = when(request.lectureType){
            LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM   -> request.credit
            LectureType.UNIVERSITY_EXPLORATION_PROGRAM      -> 0
        }

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
            completeDate = request.completeDate,
            content = request.content,
            lectureType = request.lectureType,
            credit = credit,
            instructor = user.name,
            maxRegisteredUser = request.maxRegisteredUser
        )

        lectureRepository.save(lecture)
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
                val headCount = registeredLectureRepository.countByLecture(it)
                LectureResponse.of(it,headCount)
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

        val headCount = registeredLectureRepository.countByLecture(lecture)

        val isRegistered = if(user.authority == Authority.ROLE_STUDENT) {
            val student = studentRepository findByUser user
            registeredLectureRepository.existsOne(student.id, lecture.id)
        } else false

        val response = LectureResponse.detailOf(lecture, headCount, isRegistered)

        return response
    }

    /**
     * 강의에 대해 수강신청하는 비지니스 로직입니다.
     * @param 수강신청을 하기 위한 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun signUpLecture(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository findByUser user

        val lecture = lectureRepository findById id

        if(lecture.getLectureStatus() == LectureStatus.CLOSED)
            throw NotAvailableSignUpDateException("수강신청이 가능한 시간이 아닙니다. info : [ lectureId = ${lecture.id}, currentTime = ${LocalDateTime.now()} ]")

        if(registeredLectureRepository.existsOne(student.id, lecture.id))
            throw AlreadySignedUpLectureException("이미 신청한 강의입니다. info : [ lectureId = ${lecture.id}, studentId = ${student.id} ]")

        val currentSignUpLectureStudent = registeredLectureRepository.countByLecture(lecture)

        if(lecture.maxRegisteredUser <= currentSignUpLectureStudent)
            throw OverMaxRegisteredUserException("수강 인원이 가득 찼습니다. info : [ maxRegisteredUser = ${lecture.maxRegisteredUser}, currentSignUpLectureStudent = $currentSignUpLectureStudent ]")

        val registeredLecture = RegisteredLecture(
            id = UUID.randomUUID(),
            student = student,
            lecture = lecture,
            completeDate = lecture.completeDate
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

    private infix fun LectureRepository.findById(id: UUID): Lecture = this.findByIdOrNull(id)
        ?: throw LectureNotFoundException("존재하지 않는 강의입니다. info : [ lectureId = $id ]")

    private infix fun StudentRepository.findByUser(user: User): Student = this.findByUser(user)
        ?: throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id} ]")

    private infix fun UserRepository.findById(id: UUID): User = this.findByIdOrNull(id)
        ?: throw UserNotFoundException("유저를 찾을 수 없습니다. info : [ userId = $id ]")
}