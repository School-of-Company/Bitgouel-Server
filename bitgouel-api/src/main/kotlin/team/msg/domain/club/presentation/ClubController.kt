package team.msg.domain.club.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.club.mapper.ClubRequestMapper
import team.msg.domain.club.presentation.data.response.ClubDetailsResponse
import team.msg.domain.club.presentation.data.response.ClubNamesResponse
import team.msg.domain.club.presentation.data.response.ClubsResponse
import team.msg.domain.club.presentation.web.request.CreateClubWebRequest
import team.msg.domain.club.presentation.web.request.UpdateClubWebRequest
import team.msg.domain.club.service.ClubService
import team.msg.domain.student.presentation.data.response.StudentDetailsResponse
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/club")
class ClubController(
    private val clubService: ClubService,
    private val clubRequestMapper: ClubRequestMapper
) {
    @GetMapping
    fun queryClubs(@RequestParam("highSchool") highSchool: String): ResponseEntity<ClubsResponse> {
        val response = clubService.queryClubs(highSchool)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/name")
    fun queryClubNames(@RequestParam("schoolName") schoolName: String?): ResponseEntity<ClubNamesResponse> {
        val response = clubService.queryClubNames(schoolName)
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

    @PostMapping("/{school_id}")
    fun createClub(@PathVariable("school_id") schoolId: Long, @RequestBody @Valid webRequest: CreateClubWebRequest): ResponseEntity<Unit> {
        val request = clubRequestMapper.createClubWebRequestToDto(webRequest)
        clubService.createClub(schoolId, request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateClub(@PathVariable id: Long, @RequestBody @Valid webRequest: UpdateClubWebRequest): ResponseEntity<Unit> {
        val request = clubRequestMapper.updateClubWebRequestToDto(webRequest)
        clubService.updateClub(id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteClub(@PathVariable id: Long): ResponseEntity<Unit> {
        clubService.deleteClub(id)
        return ResponseEntity.noContent().build()
    }

}