package team.msg.common.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NotBlankListValidator : ConstraintValidator<NotBlankList, List<String>> {
    override fun isValid(value: List<String>?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return false
        }
        return value.all { it.isNotBlank() }
    }
}