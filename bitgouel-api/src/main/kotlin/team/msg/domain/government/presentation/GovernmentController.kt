package team.msg.domain.government.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.government.mapper.GovernmentMapper
import team.msg.domain.government.presentation.response.GovernmentsResponse
import team.msg.domain.government.presentation.web.CreateGovernmentWebRequest
import team.msg.domain.government.service.GovernmentService

@RestController
@RequestMapping("/government")
class GovernmentController(
    private val governmentService: GovernmentService,
    private val governmentMapper: GovernmentMapper
) {
    @PostMapping
    fun createGovernment(@Valid @RequestBody webRequest: CreateGovernmentWebRequest): ResponseEntity<Unit> {
        val request = governmentMapper.createGovernmentWebRequestToDto(webRequest)
        governmentService.createGovernment(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryGovernments(): ResponseEntity<GovernmentsResponse> {
        val response = governmentService.queryGovernments()
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteGovernment(@PathVariable id: Long): ResponseEntity<Unit> {
        governmentService.deleteGovernment(id)
        return ResponseEntity.noContent().build()
    }
}