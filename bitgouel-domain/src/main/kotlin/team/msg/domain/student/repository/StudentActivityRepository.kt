package team.msg.domain.student.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import team.msg.domain.student.repository.custom.CustomStudentActivityRepository
import java.util.*

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID>, CustomStudentActivityRepository {

    @EntityGraph(attributePaths = ["student"], type = EntityGraph.EntityGraphType.FETCH)
    fun findAllByStudent(student: Student, pageable: Pageable): Page<StudentActivity>
    @EntityGraph(attributePaths = ["student"], type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(pageable: Pageable): Page<StudentActivity>
}