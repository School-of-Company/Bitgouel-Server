package team.msg.common.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NotBlankListValidator : ConstraintValidator<NotBlankList, List<String>> {
    override fun isValid(value: List<String>?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true // `@NotNull`로 null 여부는 별도로 검사
        }
        return value.all { it.isNotBlank() }
    }
}