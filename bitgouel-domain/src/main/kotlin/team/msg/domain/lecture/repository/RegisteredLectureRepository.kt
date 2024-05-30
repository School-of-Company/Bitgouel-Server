package team.msg.domain.lecture.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.student.model.Student
import java.util.*

interface RegisteredLectureRepository : CrudRepository<RegisteredLecture, UUID>, CustomRegisteredLectureRepository {

    @Query("SELECT rl FROM RegisteredLecture rl JOIN FETCH rl.student " +
            "JOIN FETCH rl.lecture r WHERE rl.student = :student")
    fun findAllByStudent(student: Student): List<RegisteredLecture>
    fun findByStudentAndLecture(student: Student, lecture: Lecture): RegisteredLecture?
    fun countByLecture(lecture: Lecture): Int
}