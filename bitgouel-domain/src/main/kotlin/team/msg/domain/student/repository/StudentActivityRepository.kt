package team.msg.domain.student.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.student.model.Student
import team.msg.domain.student.model.StudentActivity
import java.util.*

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID> {
    fun findAllByStudent(student: Student): List<StudentActivity>
    @EntityGraph(attributePaths = ["student"])
    override fun findAll(pageable: Pageable): Page<StudentActivity>
}