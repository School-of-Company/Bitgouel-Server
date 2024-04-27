package team.msg.domain.lecture.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.msg.domain.lecture.model.QLecture.lecture
import team.msg.domain.lecture.model.QLectureDate.lectureDate
import team.msg.domain.lecture.repository.custom.CustomLectureDateRepository
import java.time.LocalDate
import java.util.*

@Component
class CustomLectureDateRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomLectureDateRepository {
    /**
     * 현재 날짜 기준으로 최근 수강한 강의 날짜 조회
     * 없으면 null 반환
     *
     * @param 최근 날짜를 조회할 강의 id
     * @return 최근 수강한 강의 날짜
     */
    override fun findByCurrentCompletedDate(lectureId: UUID): LocalDate? =
        queryFactory.select(lectureDate.completeDate)
            .from(lectureDate)
            .leftJoin(lectureDate.lecture, lecture)
            .where(
                lectureDate.completeDate.before(LocalDate.now()),
                lectureDate.lecture.id.eq(lectureId)
            )
            .orderBy(
                lectureDate.completeDate.desc()
            )
            .fetchFirst()
}