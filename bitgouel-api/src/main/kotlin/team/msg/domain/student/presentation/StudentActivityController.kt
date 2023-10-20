package team.msg.domain.student.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.student.mapper.StudentActivityMapper
import team.msg.domain.student.presentation.data.web.CreateStudentActivityWebRequest
import team.msg.domain.student.service.StudentActivityService

@RestController
@RequestMapping("/activity")
class StudentActivityController(
    private val studentActivityService: StudentActivityService,
    private val studentActivityMapper: StudentActivityMapper
) {
    @PostMapping
    fun createStudentActivity(@RequestBody @Valid request: CreateStudentActivityWebRequest): ResponseEntity<Void> {
        studentActivityService.createStudentActivity(studentActivityMapper.createStudentActivityWebRequestToDto(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}