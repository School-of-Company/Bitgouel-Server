package team.msg.domain.lecture.service

import org.springframework.stereotype.Service
import team.msg.common.util.UserUtil
import team.msg.domain.lecture.enum.LectureType
import team.msg.domain.lecture.exception.InvalidLectureTypeException
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
     * @param CreateLectureRequest
     */
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
}