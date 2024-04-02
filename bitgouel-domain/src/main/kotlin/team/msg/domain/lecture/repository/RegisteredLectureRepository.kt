package team.msg.domain.lecture.repository

import javax.persistence.LockModeType
import javax.persistence.QueryHint
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.model.RegisteredLecture
import team.msg.domain.lecture.repository.custom.CustomRegisteredLectureRepository
import team.msg.domain.student.model.Student
import java.util.UUID

interface RegisteredLectureRepository : CrudRepository<RegisteredLecture, UUID>, CustomRegisteredLectureRepository {

    @Query("SELECT rl FROM RegisteredLecture rl JOIN FETCH rl.student " +
            "JOIN FETCH rl.lecture r WHERE rl.student = :student")
    fun findAllByStudent(student: Student): List<RegisteredLecture>
    fun existsByStudentAndLecture(student: Student, lecture: Lecture): Boolean
    fun findByStudentAndLecture(student: Student, lecture: Lecture): RegisteredLecture?
    @EntityGraph(attributePaths = ["student"])
    fun findAllByLecture(lecture: Lecture): List<RegisteredLecture>
    fun countByLecture(lecture: Lecture): Int
}