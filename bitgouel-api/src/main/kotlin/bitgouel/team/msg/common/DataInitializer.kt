package bitgouel.team.msg.common

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.model.School
import team.msg.domain.school.repository.SchoolRepository
import java.util.*

@Component
class DataInitializer(
    private val schoolRepository: SchoolRepository,
    private val clubRepository: ClubRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initData() {

    }

    private fun initSchool() {
        School(UUID.randomUUID())
    }

    private fun initClub() {

    }
}