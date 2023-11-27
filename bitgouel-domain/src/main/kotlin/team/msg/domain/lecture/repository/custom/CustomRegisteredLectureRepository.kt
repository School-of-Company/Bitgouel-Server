package team.msg.domain.lecture.repository.custom

import team.msg.domain.student.model.Student

interface CustomRegisteredLectureRepository {
    fun deleteAllByStudent(student: Student)
}