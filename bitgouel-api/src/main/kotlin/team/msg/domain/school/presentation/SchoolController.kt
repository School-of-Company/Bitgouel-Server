package team.msg.domain.school.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.school.mapper.SchoolRequestMapper
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest
import team.msg.domain.school.presentation.web.UpdateSchoolWebRequest
import team.msg.domain.school.service.SchoolService

@RestController
@RequestMapping("/school")
class SchoolController(
    private val schoolService: SchoolService,
    private val schoolRequestMapper: SchoolRequestMapper
) {

    @GetMapping
    fun querySchools(): ResponseEntity<SchoolsResponse> {
        val response = schoolService.querySchools()
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun createSchool(@RequestBody @Valid webRequest: CreateSchoolWebRequest): ResponseEntity<Unit> {
        val request = schoolRequestMapper.createSchoolWebRequestToDto(webRequest)
        schoolService.createSchool(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateSchool(@PathVariable id: Long, @RequestBody @Valid webRequest: UpdateSchoolWebRequest): ResponseEntity<Unit> {
        val request = schoolRequestMapper.updateSchoolWebRequestToDto(webRequest)
        schoolService.updateSchool(id, request)
        return ResponseEntity.noContent().build()
    }

}