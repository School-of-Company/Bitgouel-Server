package team.msg.domain.university.presentation

<<<<<<< HEAD
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.university.mapper.UniversityMapper
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
=======
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.university.presentation.data.response.UniversitiesResponse
>>>>>>> 6b273f0ff15c3550d54f8841a7c439853a49befc
import team.msg.domain.university.service.UniversityService

@RestController
@RequestMapping("/university")
class UniversityController(
<<<<<<< HEAD
    private val universityService: UniversityService,
    private val universityMapper: UniversityMapper
) {

    @PostMapping
    fun createUniversity(@RequestBody webRequest: CreateUniversityWebRequest): ResponseEntity<Unit> {
        val request = universityMapper.createUniversityWebRequestToDto(webRequest)
        universityService.createUniversity(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
=======
    private val universityService: UniversityService
) {
    @GetMapping
    fun queryUniversities(): ResponseEntity<UniversitiesResponse> {
        val response = universityService.queryUniversities()
        return ResponseEntity.ok(response)
>>>>>>> 6b273f0ff15c3550d54f8841a7c439853a49befc
    }
}