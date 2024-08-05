package team.msg.domain.school.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.custom.CustomSchoolRepository

interface SchoolRepository : CrudRepository<School, Long>, CustomSchoolRepository {
    fun findByName(name: String): School?
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNotLike(name: String, id: Long): Boolean
}