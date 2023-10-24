package team.msg.domain.lecture.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.student.model.Student
import java.util.UUID

interface RegisteredLectureRepository : CrudRepository<RegisteredLecture,UUID> {
    fun findAllByStudent(student: Student): List<RegisteredLecture>
}