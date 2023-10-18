package team.msg.domain.club.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.club.model.Club
import team.msg.domain.school.model.School

interface ClubRepository : JpaRepository<Club, Long> {
    fun findByNameAndSchool(name: String, school: School): Club?
}