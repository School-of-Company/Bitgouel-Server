package team.msg.domain.lecture.service

import team.msg.domain.lecture.presentation.data.request.LectureCreateRequest

interface LectureService {
    fun lectureCreate(request: LectureCreateRequest)
}