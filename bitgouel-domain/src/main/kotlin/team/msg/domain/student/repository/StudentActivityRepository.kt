package team.msg.domain.student.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.student.model.StudentActivity
import java.util.UUID

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID>