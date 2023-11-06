package team.msg.domain.club.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.club.presentation.data.response.ClubResponse
import team.msg.domain.club.repository.ClubRepository
import team.msg.domain.school.model.School

@Service
class ClubServiceImpl(
    private val clubRepository: ClubRepository
) : ClubService {

    @Transactional(readOnly = true)
    override fun queryAllClubsService(school: School): AllClubResponse {
        val clubs = clubRepository.findAllBySchool(school)

        val response = AllClubResponse(
            ClubResponse.listOf(clubs)
        )

        return response
    }
}