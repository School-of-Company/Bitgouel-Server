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
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest
import team.msg.domain.lecture.repository.LectureRepository
import java.util.*

@Service
class LectureServiceImpl(
    private val lectureRepository: LectureRepository,
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
     * 강의 개설 신청을 수락하는 비지니스 로직입니다.
     * @param 승인할 강의의 id
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
            lectureType = lecture.lectureType,
            credit = lecture.credit,
            instructor = lecture.user.name,
            maxRegisteredUser = lecture.maxRegisteredUser
        )

        lectureRepository.save(approveLecture)
    }

    /**
     * 강의 개설 신청을 거절하는 비지니스 로직입니다.
     * @param 거절할 강의의 id
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