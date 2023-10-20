package team.msg.domain.lecture.service

import team.msg.domain.lecture.presentation.data.request.CreateLectureRequest

interface LectureService {
    fun createLecture(request: CreateLectureRequest)
}