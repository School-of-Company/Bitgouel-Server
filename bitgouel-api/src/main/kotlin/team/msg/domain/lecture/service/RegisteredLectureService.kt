package team.msg.domain.lecture.service

import team.msg.domain.lecture.model.Lecture
import team.msg.domain.student.model.Student

interface RegisteredLectureService {
    fun validateRegisterLectureCondition(student: Student, lecture: Lecture)
}