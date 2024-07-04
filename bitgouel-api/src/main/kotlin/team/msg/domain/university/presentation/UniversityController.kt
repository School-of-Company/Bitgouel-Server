package team.msg.domain.university.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.university.mapper.UniversityRequestMapper
import team.msg.domain.university.presentation.data.response.UniversitiesResponse
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
import team.msg.domain.university.service.UniversityService

@RestController
@RequestMapping("/university")
class UniversityController(
    private val universityService: UniversityService,
    private val universityRequestMapper: UniversityRequestMapper
) {

    @PostMapping
    fun createUniversity(@RequestBody webRequest: CreateUniversityWebRequest): ResponseEntity<Unit> {
        val request = universityRequestMapper.createUniversityWebRequestToDto(webRequest)
        universityService.createUniversity(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{id}")
    fun deleteUniversity(@PathVariable id: Long): ResponseEntity<Unit> {
        universityService.deleteUniversity(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun queryUniversities(): ResponseEntity<UniversitiesResponse> {
        val response = universityService.queryUniversities()
        return ResponseEntity.ok(response)
    }
}