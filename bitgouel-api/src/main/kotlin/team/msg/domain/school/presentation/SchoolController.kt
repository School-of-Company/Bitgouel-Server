package team.msg.domain.school.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import team.msg.domain.school.mapper.SchoolRequestMapper
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.presentation.web.CreateSchoolWebRequest
import team.msg.domain.school.presentation.web.UpdateSchoolWebRequest
import team.msg.domain.school.service.SchoolService
import javax.validation.Valid

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
    fun createSchool(@RequestPart("webRequest") @Valid webRequest: CreateSchoolWebRequest, @RequestPart("logoImage") logoImage: MultipartFile): ResponseEntity<Unit> {
        val request = schoolRequestMapper.createSchoolWebRequestToDto(webRequest)
        schoolService.createSchool(request, logoImage)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateSchool(@PathVariable id: Long, @RequestPart @Valid webRequest: UpdateSchoolWebRequest, @RequestPart("logoImage") logoImage: MultipartFile): ResponseEntity<Unit> {
        val request = schoolRequestMapper.updateSchoolWebRequestToDto(webRequest)
        schoolService.updateSchool(id, request, logoImage)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("{id}")
    fun deleteSchool(@PathVariable id: Long): ResponseEntity<Unit> {
        schoolService.deleteSchool(id)
        return ResponseEntity.noContent().build()
    }

}