package team.msg.domain.certification.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.certification.mapper.CertificationRequestMapper
import team.msg.domain.certification.presentation.data.response.CertificationsResponse
import team.msg.domain.certification.presentation.data.web.CreateCertificationWebRequest
import team.msg.domain.certification.service.CertificationService

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
    fun queryAllCertifications(): ResponseEntity<CertificationsResponse> {
        val response = certificationService.queryCertifications()
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}