package team.msg.domain.lecture.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.lecture.enums.LectureStatus
import team.msg.domain.lecture.exception.AlreadySignedUpLectureException
import team.msg.domain.lecture.exception.NotAvailableSignUpDateException
import team.msg.domain.lecture.exception.OverMaxRegisteredUserException
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.repository.RegisteredLectureRepository
import team.msg.domain.student.model.Student
import java.time.LocalDateTime

@Service
class RegisteredLectureServiceImpl(
    private val registeredLectureRepository: RegisteredLectureRepository
) : RegisteredLectureService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun validateRegisterLectureCondition(student: Student, lecture: Lecture) {
        if(lecture.getLectureStatus() == LectureStatus.CLOSED)
            throw NotAvailableSignUpDateException("수강신청이 가능한 시간이 아닙니다. info : [ lectureId = ${lecture.id}, currentTime = ${LocalDateTime.now()} ]")

        if(registeredLectureRepository.existsOne(student.id, lecture.id))
            throw AlreadySignedUpLectureException("이미 신청한 강의입니다. info : [ lectureId = ${lecture.id}, studentId = ${student.id} ]")

        val currentSignUpLectureStudent = registeredLectureRepository.countRegisteredLectureByLecture(lecture)

        if(lecture.maxRegisteredUser <= currentSignUpLectureStudent)
            throw OverMaxRegisteredUserException("수강 인원이 가득 찼습니다. info : [ maxRegisteredUser = ${lecture.maxRegisteredUser}, currentSignUpLectureStudent = $currentSignUpLectureStudent ]")
    }
}