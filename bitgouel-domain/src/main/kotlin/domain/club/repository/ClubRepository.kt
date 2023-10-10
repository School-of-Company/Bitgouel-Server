package domain.club.repository

import domain.club.model.Club
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ClubRepository : JpaRepository<Club, UUID> {
}