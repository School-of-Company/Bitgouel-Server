package domain.student.repository

import domain.student.model.StudentActivity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentActivityRepository : JpaRepository<StudentActivity, UUID>