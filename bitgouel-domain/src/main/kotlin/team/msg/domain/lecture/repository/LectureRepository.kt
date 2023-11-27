package team.msg.domain.lecture.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.lecture.model.Lecture
import team.msg.domain.lecture.repository.custom.CustomLectureRepository
import java.util.*


interface LectureRepository : JpaRepository<Lecture, UUID>, CustomLectureRepository