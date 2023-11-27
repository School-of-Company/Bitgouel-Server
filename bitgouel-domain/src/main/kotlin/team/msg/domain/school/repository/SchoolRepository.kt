package team.msg.domain.school.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.custom.CustomSchoolRepository

interface SchoolRepository : CrudRepository<School, Long>,CustomSchoolRepository {
    fun findByHighSchool(highSchool: HighSchool): School?
}