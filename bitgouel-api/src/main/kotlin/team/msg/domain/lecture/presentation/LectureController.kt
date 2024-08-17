package team.msg.domain.lecture.presentation

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team.msg.domain.lecture.mapper.LectureRequestMapper
import team.msg.domain.lecture.presentation.data.response.*
import team.msg.domain.lecture.presentation.data.web.*
import team.msg.domain.lecture.service.LectureService
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

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

    @PatchMapping("/{id}")
    fun updateLecture(@PathVariable id: UUID, @Valid @RequestBody webRequest: UpdateLectureWebRequest): ResponseEntity<Unit> {
        val request = lectureRequestMapper.updateLectureWebRequestToDto(webRequest)
        lectureService.updateLecture(id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}/soft")
    fun deleteLecture(@PathVariable id: UUID): ResponseEntity<Unit> {
        lectureService.deleteLecture(id)
        return ResponseEntity.noContent().build()
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

    @GetMapping("/excel")
    fun lectureReceiptStatusExcel(response: HttpServletResponse): ResponseEntity<Unit> {
        lectureService.lectureReceiptStatusExcel(response)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{student_id}/signup")
    fun queryAllSignedUpLectures(@PathVariable("student_id") studentId: UUID): ResponseEntity<SignedUpLecturesResponse> {
        val response = lectureService.queryAllSignedUpLectures(studentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/student/{id}")
    fun queryAllSignedUpStudents(@PathVariable id: UUID, webRequest: QueryAllSignedUpStudentsWebRequest): ResponseEntity<SignedUpStudentsResponse> {
        val request = lectureRequestMapper.queryAllSignedUpStudentsWebRequestToDto(webRequest)
        val response = lectureService.queryAllSignedUpStudents(id, request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/student/{id}/{student_id}")
    fun querySignedUpStudentDetails(@PathVariable id: UUID, @PathVariable("student_id") studentId: UUID): ResponseEntity<SignedUpStudentDetailsResponse> {
        val response = lectureService.querySignedUpStudentDetails(id, studentId)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}/complete")
    fun updateLectureCompleteStatus(@PathVariable id: UUID, @RequestParam studentIds: List<UUID>): ResponseEntity<Unit> {
        lectureService.updateLectureCompleteStatus(id, studentIds)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/cancel")
    fun cancelLectureCompleteStatus(@PathVariable id: UUID, @RequestParam studentIds: List<UUID>): ResponseEntity<Unit> {
        lectureService.cancelLectureCompleteStatus(id, studentIds)
        return ResponseEntity.noContent().build()
    }

}