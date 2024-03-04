package team.msg.domain.post.presentation.web.validation

import javax.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [URLListValidator::class])
annotation class URLList (
    val message: String = "올바른 URL 형식이 아닙니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = [],
)