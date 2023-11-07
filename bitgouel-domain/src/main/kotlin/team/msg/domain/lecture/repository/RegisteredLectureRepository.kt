package team.msg.domain.lecture.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.student.model.Student
import java.util.UUID

interface RegisteredLectureRepository : CrudRepository<RegisteredLecture, UUID> {

    @Query("SELECT rl FROM RegisteredLecture rl JOIN FETCH rl.student " +
            "JOIN FETCH rl.lecture r WHERE rl.student = :student")
    fun findAllByStudent(student: Student): List<RegisteredLecture>
    fun existsByStudentAndLecture(student: Student, lecture: Lecture): Boolean
    fun countByLecture(lecture: Lecture): Int
}