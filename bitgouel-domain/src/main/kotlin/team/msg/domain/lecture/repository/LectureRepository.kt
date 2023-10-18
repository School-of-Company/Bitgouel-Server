package team.msg.domain.lecture.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.msg.domain.lecture.model.Lecture
import java.util.UUID

interface LectureRepository : JpaRepository<Lecture, UUID>