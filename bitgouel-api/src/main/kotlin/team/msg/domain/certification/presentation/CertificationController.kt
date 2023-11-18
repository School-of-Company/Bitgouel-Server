package team.msg.domain.certification.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.certification.mapper.CertificationRequestMapper
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.presentation.data.web.UpdateCertificationWebRequest
import team.msg.domain.certification.service.CertificationService
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/certification")
class CertificationController(
    private val certificationService: CertificationService,
    private val certificationRequestMapper: CertificationRequestMapper
) {
    @PostMapping
    fun createCertification(@RequestBody @Valid webRequest: CreateCertificationWebRequest): ResponseEntity<Void> {
        val request = certificationRequestMapper.createCertificationWebRequestToDto(webRequest)
        certificationService.createCertification(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryCertifications(): ResponseEntity<CertificationsResponse> {
        val response = certificationService.queryCertifications()
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/{student_id}")
    fun queryCertifications(@PathVariable("student_id") studentId: UUID): ResponseEntity<CertificationsResponse> {
        val response = certificationService.queryCertifications(studentId)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @PatchMapping("/{id}")
    fun updateCertification(@PathVariable id: UUID, @RequestBody @Valid webRequest: UpdateCertificationWebRequest): ResponseEntity<Void> {
        val request = certificationRequestMapper.updateCertificationWebRequestToDto(webRequest)
        certificationService.updateCertification(id, request)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}