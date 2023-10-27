package team.msg.domain.lecture.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.common.enum.ApproveStatus
import team.msg.common.util.UserUtil
import team.msg.domain.lecture.enum.LectureType
import team.msg.domain.lecture.exception.AlreadyApprovedLectureException
import team.msg.domain.lecture.exception.InvalidLectureTypeException
import team.msg.domain.lecture.exception.LectureNotFoundException
import team.msg.domain.lecture.exception.MissSignUpAbleDateException
import team.msg.domain.lecture.exception.NotApprovedLectureException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.repository.LectureRepository
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.student.exception.StudentNotFoundException
import team.msg.domain.student.repository.StudentRepository
import java.time.LocalDateTime
import java.util.*

@Service
class LectureServiceImpl(
    private val lectureRepository: LectureRepository,
    private val registeredLectureRepository: RegisteredLectureRepository,
    private val studentRepository: StudentRepository,
    private val userUtil: UserUtil
) : LectureService{

    /**
     * 강의 개설을 처리하는 비지니스 로직입니다.
     * @param 생성할 강의의 데이터를 담은 request Dto
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun createLecture(request: CreateLectureRequest) {
        val user = userUtil.queryCurrentUser()

        val credit = when(request.lectureType){
            LectureType.MUTUAL_CREDIT_RECOGNITION_PROGRAM   -> request.credit
            LectureType.UNIVERSITY_EXPLORATION_PROGRAM      -> 0
            else -> throw InvalidLectureTypeException("유효하지 않은 강의 구분입니다. info : [ type = ${request.lectureType} ]")
        }

        val lecture = Lecture(
            id = UUID.randomUUID(),
            user = user,
            name = request.name,
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
     * 강의에 대해 수강신청하는 비지니스 로직입니다.
     * @param 수강신청을 하기 위한 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun signUpLecture(id: UUID) {
        val user = userUtil.queryCurrentUser()

        val student = studentRepository.findByIdOrNull(user.id) ?:
            throw StudentNotFoundException("학생을 찾을 수 없습니다. info : [ userId = ${user.id}, userName = ${user.name} ]")

        val lecture = queryLecture(id)

        if(lecture.approveStatus == ApproveStatus.PENDING)
            throw NotApprovedLectureException("아직 승인되지 않은 강의입니다. info : [ lectureId = ${lecture.id} ]")

        if(lecture.startDate.isBefore(LocalDateTime.now()))
            throw MissSignUpAbleDateException("이른 강의 신청입니다. info : [ lectureStartDate = ${lecture.startDate}, currentDate = ${LocalDateTime.now()} ]")

        if(lecture.endDate.isAfter(LocalDateTime.now()))
            throw MissSignUpAbleDateException("늦은 강의 신청입니다. info : [ lectureEndDate = ${lecture.endDate}, currentDate = ${LocalDateTime.now()} ]")

        val currentSignUpLectureStudent = registeredLectureRepository.findAllByLecture(lecture).size

        if(lecture.maxRegisteredUser == currentSignUpLectureStudent)
            throw OverMaxRegisteredUserException("수강 인원이 가득 찼습니다. info : [ maxRegisteredUser = ${lecture.maxRegisteredUser}, currentSignUpLectureStudent = $currentSignUpLectureStudent ]")

        val registeredLecture = RegisteredLecture(
            id = UUID.randomUUID(),
            student = student,
            lecture = lecture,
            completeDate = lecture.completeDate
        )

        registeredLectureRepository.save(registeredLecture)

    }

    /**
     * 강의 개설 신청을 수락하는 비지니스 로직입니다.
     * @param 개설을 수락할 대기 상태의 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun approveLecture(id: UUID) {
        val lecture = queryLecture(id)

        if(lecture.approveStatus == ApproveStatus.APPROVED)
            throw AlreadyApprovedLectureException("이미 개설 신청이 승인된 강의입니다. info : [ lectureId = $id ]")

        val approveLecture = Lecture(
            id = lecture.id,
            user = lecture.user,
            name = lecture.name,
            startDate = lecture.startDate,
            endDate = lecture.endDate,
            completeDate = lecture.completeDate,
            content = lecture.content,
            lectureType =  lecture.lectureType,
            credit = lecture.credit,
            instructor = lecture.instructor,
            maxRegisteredUser = lecture.maxRegisteredUser,
            approveStatus = ApproveStatus.APPROVED
        )

        lectureRepository.save(approveLecture)
    }

    /**
     * 강의 개설 신청을 거절하는 비지니스 로직입니다.
     * @param 개설을 거절할 대기 상태의 강의 id
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun rejectLecture(id: UUID) {
        val lecture = queryLecture(id)

        if(lecture.approveStatus == ApproveStatus.APPROVED)
            throw AlreadyApprovedLectureException("이미 개설 신청이 승인된 강의입니다. info : [ lectureId = $id ]")

        lectureRepository.delete(lecture)
    }

    private fun queryLecture(id: UUID) = lectureRepository.findByIdOrNull(id)
            ?: throw LectureNotFoundException("존재하지 않는 강의입니다. info : [ lectureId = $id ]")
}