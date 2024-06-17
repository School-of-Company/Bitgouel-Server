package team.msg.domain.lecture.presentation

import javax.servlet.http.HttpServletResponse
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.lecture.mapper.LectureRequestMapper
import team.msg.domain.lecture.presentation.data.response.DepartmentsResponse
import team.msg.domain.lecture.presentation.data.response.DivisionsResponse
import team.msg.domain.lecture.presentation.data.response.InstructorsResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.data.response.LecturesResponse
import team.msg.domain.lecture.presentation.data.response.LinesResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpLecturesResponse
import team.msg.domain.lecture.presentation.data.response.SignedUpStudentsResponse
import team.msg.domain.lecture.presentation.data.web.CreateLectureWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDepartmentsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllDivisionsWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLecturesWebRequest
import team.msg.domain.lecture.presentation.data.web.QueryAllLinesWebRequest
import team.msg.domain.lecture.service.LectureService
import java.util.*

@RestController
@RequestMapping("/lecture")
class LectureController(
    private val lectureRequestMapper: LectureRequestMapper,
    private val lectureService: LectureService
) {
    @PostMapping
    fun createLecture(@Valid @RequestBody webRequest: CreateLectureWebRequest): ResponseEntity<Unit> {
        val request = lectureRequestMapper.createLectureWebRequestToDto(webRequest)
        lectureService.createLecture(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryAllLectures(pageable: Pageable, webRequest: QueryAllLecturesWebRequest): ResponseEntity<LecturesResponse>{
        val request = lectureRequestMapper.queryLectureWebRequestToDto(webRequest)
        val response = lectureService.queryAllLectures(pageable, request)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @PostMapping("/{id}")
    fun signUpLecture(@PathVariable id: UUID): ResponseEntity<Unit> {
        lectureService.signUpLecture(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun cancelSignUpLecture(@PathVariable id: UUID): ResponseEntity<Unit> {
        lectureService.cancelSignUpLecture(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun queryLectureDetails(@PathVariable id: UUID): ResponseEntity<LectureDetailsResponse> {
        val response = lectureService.queryLectureDetails(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/instructor")
    fun queryInstructors(@RequestParam keyword: String): ResponseEntity<InstructorsResponse> {
        val response = lectureService.queryInstructors(keyword)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/line")
    fun queryAllLines(webRequest: QueryAllLinesWebRequest): ResponseEntity<LinesResponse> {
        val request = lectureRequestMapper.queryAllLinesWebRequestToDto(webRequest)
        val response = lectureService.queryAllLines(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/department")
    fun queryAllDepartments(webRequest: QueryAllDepartmentsWebRequest): ResponseEntity<DepartmentsResponse> {
        val request = lectureRequestMapper.queryAllDepartmentsWebRequestToDto(webRequest)
        val response = lectureService.queryAllDepartments(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/division")
    fun queryAllDivisions(webRequest: QueryAllDivisionsWebRequest): ResponseEntity<DivisionsResponse> {
        val request = lectureRequestMapper.queryAllDivisionsWebRequestToDto(webRequest)
        val response = lectureService.queryAllDivisions(request)
        return ResponseEntity.ok(response)
    }

//    @GetMapping("/excel")
//    fun lectureReceiptStatusExcel(response: HttpServletResponse): ResponseEntity<Unit> {
//        lectureService.lectureReceiptStatusExcel(response)
//        return ResponseEntity.ok().build()
//    }

    @GetMapping("/{student_id}/signup")
    fun queryAllSignedUpLectures(@PathVariable("student_id") studentId: UUID): ResponseEntity<SignedUpLecturesResponse> {
        val response = lectureService.queryAllSignedUpLectures(studentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/student/{id}")
    fun queryAllSignedUpStudents(@PathVariable id: UUID): ResponseEntity<SignedUpStudentsResponse> {
        val response = lectureService.queryAllSignedUpStudents(id)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}/{student_id}")
    fun updateLectureCompleteStatus(
        @PathVariable id: UUID, @PathVariable("student_id") studentId: UUID,
        @RequestParam isComplete: Boolean
    ): ResponseEntity<Unit> {
        lectureService.updateLectureCompleteStatus(id, studentId, isComplete)
        return ResponseEntity.noContent().build()
    }
}