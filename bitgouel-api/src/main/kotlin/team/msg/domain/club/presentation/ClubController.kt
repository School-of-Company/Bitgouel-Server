package team.msg.domain.club.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.club.presentation.data.response.AllClubResponse
import team.msg.domain.club.service.ClubService
import team.msg.domain.school.enums.HighSchool

@RestController
@RequestMapping("/club")
class ClubController(
    private val clubService: ClubService
) {
    /**
     * 모든 동아리를 조회하는 비즈니스 로직
     * @param 동아리를 조회하기 위한 학교 이름
     */
    @GetMapping
    fun queryAllClubs(@RequestParam("highSchool") highSchool: HighSchool): ResponseEntity<AllClubResponse> {
        val response = clubService.queryAllClubsService(highSchool)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}