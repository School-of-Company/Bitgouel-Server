package team.msg.domain.lecture.repository

import org.springframework.data.repository.CrudRepository
import team.msg.domain.lecture.model.LectureLocation

interface LectureLocationRepository : CrudRepository<LectureLocation, Long>
