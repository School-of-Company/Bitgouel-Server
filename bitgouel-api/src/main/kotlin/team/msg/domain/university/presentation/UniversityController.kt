package team.msg.domain.university.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.university.mapper.UniversityRequestMapper
import team.msg.domain.university.presentation.data.response.UniversitiesResponse
import team.msg.domain.university.presentation.data.web.CreateDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.CreateUniversityWebRequest
import team.msg.domain.university.presentation.data.web.DeleteDepartmentWebRequest
import team.msg.domain.university.presentation.data.web.UpdateUniversityWebRequest
import team.msg.domain.university.service.UniversityService

@RestController
@RequestMapping("/university")
class UniversityController(
    private val universityService: UniversityService,
    private val universityRequestMapper: UniversityRequestMapper
) {

    @PostMapping
    fun createUniversity(@RequestBody @Valid webRequest: CreateUniversityWebRequest): ResponseEntity<Unit> {
        val request = universityRequestMapper.createUniversityWebRequestToDto(webRequest)
        universityService.createUniversity(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateUniversity(@PathVariable id: Long, @RequestBody @Valid webRequest: UpdateUniversityWebRequest): ResponseEntity<Unit> {
        val request = universityRequestMapper.updateUniversityWebRequestToDto(webRequest)
        universityService.updateUniversity(id, request)
        return ResponseEntity.noContent().build()
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

    @PostMapping("/department/{id}")
    fun createDepartment(@PathVariable id: Long, @RequestBody @Valid webRequest: CreateDepartmentWebRequest): ResponseEntity<Unit> {
        val request = universityRequestMapper.createDepartmentWebRequestToDto(webRequest)
        universityService.createDepartment(id, request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/department/{id}")
    fun deleteDepartment(@PathVariable id: Long, @RequestBody webRequest: DeleteDepartmentWebRequest): ResponseEntity<Unit> {
        val request = universityRequestMapper.deleteDepartmentWebRequestToDto(webRequest)
        universityService.deleteDepartment(id, request)
        return ResponseEntity.noContent().build()
    }
}