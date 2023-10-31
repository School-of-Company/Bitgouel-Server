package team.msg.domain.lecture.presentation

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
import team.msg.domain.lecture.mapper.LectureRequestMapper
import team.msg.domain.lecture.presentation.data.response.AllLecturesResponse
import team.msg.domain.lecture.presentation.data.response.LectureDetailsResponse
import team.msg.domain.lecture.presentation.web.CreateLectureWebRequest
import team.msg.domain.lecture.presentation.web.QueryAllLecturesWebRequest
import team.msg.domain.lecture.service.LectureService
import java.util.*

@RestController
@RequestMapping("/lecture")
class LectureController(
    private val lectureRequestMapper: LectureRequestMapper,
    private val lectureService: LectureService
) {
    @PostMapping
    fun createLecture(@Valid @RequestBody webRequest: CreateLectureWebRequest): ResponseEntity<Void> {
        val request = lectureRequestMapper.createLectureWebRequestToDto(webRequest)
        lectureService.createLecture(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun queryAllLectures(pageable: Pageable, queryAllLecturesWebRequest: QueryAllLecturesWebRequest): ResponseEntity<AllLecturesResponse>{
        val request = lectureRequestMapper.queryLectureWebRequestToDto(queryAllLecturesWebRequest)
        val response = lectureService.queryAllLectures(pageable, request)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @PostMapping("/{id}")
    fun signUpLecture(@PathVariable id: UUID): ResponseEntity<Void> {
        lectureService.signUpLecture(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping("/{id}")
    fun queryLectureDetails(@PathVariable id: UUID): ResponseEntity<LectureDetailsResponse> {
        val response = lectureService.queryLectureDetails(id)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @PatchMapping("/{id}/approve")
    fun approveLecture(@PathVariable id: UUID): ResponseEntity<Void> {
        lectureService.approveLecture(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}/delete")
    fun rejectLecture(@PathVariable id: UUID): ResponseEntity<Void> {
        lectureService.approveLecture(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}