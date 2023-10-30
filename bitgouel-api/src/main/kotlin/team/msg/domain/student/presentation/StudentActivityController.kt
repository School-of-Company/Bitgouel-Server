package team.msg.domain.student.presentation

import javax.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.student.mapper.StudentActivityMapper
import team.msg.domain.student.presentation.data.response.AllStudentActivityResponse
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest
import team.msg.domain.student.presentation.data.web.UpdateStudentActivityWebRequest
import team.msg.domain.student.service.StudentActivityService
import java.util.*

@RestController
@RequestMapping("/activity")
class StudentActivityController(
    private val studentActivityService: StudentActivityService,
    private val studentActivityMapper: StudentActivityMapper
) {
    @PostMapping
    fun createStudentActivity(@RequestBody @Valid webRequest: CreateStudentActivityWebRequest): ResponseEntity<Void> {
        val request = studentActivityMapper.createStudentActivityWebRequestToDto(webRequest)
        studentActivityService.createStudentActivity(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{id}")
    fun updateStudentActivity(@PathVariable id: UUID, @RequestBody @Valid webRequest: UpdateStudentActivityWebRequest): ResponseEntity<Void> {
        val request = studentActivityMapper.updateStudentActivityWebRequestToDto(webRequest)
        studentActivityService.updateStudentActivity(id, request)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PatchMapping("/{id}/approve")
    fun approveStudentActivity(@PathVariable id:UUID): ResponseEntity<Void> {
        studentActivityService.approveStudentActivity(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun deleteStudentActivity(@PathVariable id: UUID): ResponseEntity<Void> {
        studentActivityService.deleteStudentActivity(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}/reject")
    fun rejectStudentActivity(@PathVariable id: UUID): ResponseEntity<Void> {
        studentActivityService.rejectStudentActivity(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping
    fun queryAllStudentActivity(pageable: Pageable): ResponseEntity<AllStudentActivityResponse> {
        val response = studentActivityService.queryAllStudentActivity(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}