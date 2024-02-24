package team.msg.domain.club.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.club.presentation.data.response.MyClubDetailsResponse
import team.msg.domain.club.service.ClubService
import team.msg.domain.school.enums.HighSchool
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import java.util.*

@RestController
@RequestMapping("/club")
class ClubController(
    private val clubService: ClubService
) {
    @GetMapping
    fun queryAllClubs(@RequestParam("highSchool") highSchool: HighSchool): ResponseEntity<ClubsResponse> {
        val response = clubService.queryAllClubs(highSchool)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/{id}")
    fun queryClubDetails(@PathVariable id: Long): ResponseEntity<ClubDetailsResponse> {
        val response = clubService.queryClubDetailsById(id)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/my")
    fun queryMyClubDetails(): ResponseEntity<MyClubDetailsResponse> {
        val response = clubService.queryMyClubDetails()
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/{id}/{student_id}")
    fun queryStudentDetails(@PathVariable("id") clubId: Long, @PathVariable("student_id") studentId: UUID): ResponseEntity<StudentDetailsResponse> {
        val response = clubService.queryStudentDetails(clubId, studentId)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}