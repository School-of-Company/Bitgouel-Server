package team.msg.domain.lecture.presentation

import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.lecture.mapper.LectureRequestMapper
import team.msg.domain.lecture.presentation.data.web.LectureCreateWebRequest
import team.msg.domain.lecture.service.LectureService

@RestController
@RequestMapping("/lecture")
class LectureController(
    private val lectureRequestMapper: LectureRequestMapper,
    private val lectureService: LectureService
) {
    @PostMapping
    fun lectureCreate(@Valid @RequestBody webRequest: LectureCreateWebRequest) : ResponseEntity<Void> {
        lectureService.lectureCreate(lectureRequestMapper.lectureCreateWebRequestToDto(webRequest))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}