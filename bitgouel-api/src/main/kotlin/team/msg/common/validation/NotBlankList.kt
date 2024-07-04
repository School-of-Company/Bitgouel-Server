package team.msg.common.validation

import javax.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotBlankListValidator::class])
annotation class NotBlankList(
    val message: String = "리스트 내의 모든 요소는 빈 문자열일 수 없습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)