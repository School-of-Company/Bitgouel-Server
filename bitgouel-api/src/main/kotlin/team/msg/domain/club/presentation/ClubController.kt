package team.msg.domain.club.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.club.service.ClubService
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import java.util.*

@RestController
@RequestMapping("/club")
class ClubController(
    private val clubService: ClubService
) {
    @GetMapping
    fun queryAllClubs(@RequestParam("highSchool") highSchool: String): ResponseEntity<ClubsResponse> {
        val response = clubService.queryAllClubs(highSchool)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun queryClubDetails(@PathVariable id: Long): ResponseEntity<ClubDetailsResponse> {
        val response = clubService.queryClubDetailsById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my")
    fun queryMyClubDetails(): ResponseEntity<ClubDetailsResponse> {
        val response = clubService.queryMyClubDetails()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}/{student_id}")
    fun queryStudentDetails(@PathVariable("id") clubId: Long, @PathVariable("student_id") studentId: UUID): ResponseEntity<StudentDetailsResponse> {
        val response = clubService.queryStudentDetails(clubId, studentId)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteClub(@PathVariable id: Long): ResponseEntity<Unit> {
        clubService.deleteClub(id)
        return ResponseEntity.noContent().build()
    }
}