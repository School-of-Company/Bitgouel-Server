package team.msg.domain.school.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.school.model.School
import java.util.UUID

interface SchoolRepository : CrudRepository<School, UUID> {
}