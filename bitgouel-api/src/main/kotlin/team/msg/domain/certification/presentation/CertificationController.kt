package team.msg.domain.certification.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.certification.mapper.CertificationRequestMapper
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.presentation.data.web.UpdateCertificationWebRequest
import team.msg.domain.certification.service.CertificationService
import java.util.*

@RestController
@RequestMapping("/certification")
class CertificationController(
    private val certificationService: CertificationService,
    private val certificationRequestMapper: CertificationRequestMapper
) {
    @PostMapping
    fun createCertification(@RequestBody @Valid webRequest: CreateCertificationWebRequest): ResponseEntity<Unit> {
        val request = certificationRequestMapper.createCertificationWebRequestToDto(webRequest)
        certificationService.createCertification(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryCertifications(): ResponseEntity<CertificationsResponse> {
        val response = certificationService.queryCertifications()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{student_id}")
    fun queryCertifications(@PathVariable("student_id") studentId: UUID): ResponseEntity<CertificationsResponse> {
        val response = certificationService.queryCertifications(studentId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}")
    fun updateCertification(@PathVariable id: UUID, @RequestBody @Valid webRequest: UpdateCertificationWebRequest): ResponseEntity<Unit> {
        val request = certificationRequestMapper.updateCertificationWebRequestToDto(webRequest)
        certificationService.updateCertification(id, request)
        return ResponseEntity.noContent().build()
    }
}