package team.msg.domain.school.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.school.model.School

interface SchoolRepository : CrudRepository<School, Long>, CustomSchoolRepository {
    fun findByHighSchool(highSchool: HighSchool): School?
}