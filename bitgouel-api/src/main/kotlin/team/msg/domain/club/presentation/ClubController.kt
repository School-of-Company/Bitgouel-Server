package team.msg.domain.club.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.service.ClubService
import team.msg.domain.school.enums.HighSchool

@RestController
@RequestMapping("/club")
class ClubController(
    private val clubService: ClubService
) {
    @GetMapping
    fun queryAllClubs(@RequestParam("highSchool") highSchool: HighSchool): ResponseEntity<AllClubResponse> {
        val response = clubService.queryAllClubsService(highSchool)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/{id}")
    fun queryClubDetails(@PathVariable id: Long): ResponseEntity<ClubDetailsResponse> {
        val response = clubService.queryClubDetailsService(id)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}