package team.msg.domain.school.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.school.presentation.data.response.SchoolsResponse
import team.msg.domain.school.service.SchoolService

@RestController
@RequestMapping("/school")
class SchoolController(
    private val schoolService: SchoolService
) {
    @GetMapping
    fun querySchools(): ResponseEntity<SchoolsResponse> {
        val response = schoolService.querySchools()
        return ResponseEntity.ok(response)
    }
}