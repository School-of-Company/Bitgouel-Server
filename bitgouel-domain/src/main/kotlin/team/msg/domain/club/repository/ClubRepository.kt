package team.msg.domain.club.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.club.model.Club
import java.util.UUID

interface ClubRepository : JpaRepository<Club, Int> {
}