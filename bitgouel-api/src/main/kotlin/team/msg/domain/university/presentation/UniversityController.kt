package team.msg.domain.university.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.university.mapper.UniversityMapper
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
import team.msg.domain.university.service.UniversityService

@RestController
@RequestMapping("/university")
class UniversityController(
    private val universityService: UniversityService,
    private val universityMapper: UniversityMapper
) {

    @PostMapping
    fun createUniversity(@RequestBody webRequest: CreateUniversityWebRequest): ResponseEntity<Unit> {
        val request = universityMapper.createUniversityWebRequestToDto(webRequest)
        universityService.createUniversity(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}