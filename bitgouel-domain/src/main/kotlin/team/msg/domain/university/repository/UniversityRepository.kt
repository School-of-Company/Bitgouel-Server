package team.msg.domain.university.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.university.model.University

interface UniversityRepository : CrudRepository<University, Long> {
    fun findByName(name: String): University?
    fun existsByName(name: String): Boolean
    override fun findAll(): List<University>
}