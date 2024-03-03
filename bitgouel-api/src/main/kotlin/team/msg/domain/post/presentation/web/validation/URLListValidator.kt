package team.msg.domain.post.presentation.web.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class URLListValidator : ConstraintValidator<URLList, List<String>> {
    override fun isValid(values: List<String>?, context: ConstraintValidatorContext?): Boolean {
        val urls = values ?: return false

        val regex = Regex("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

        return urls.none { regex.notMatches(it) || it.length > 2083 }
    }
}

fun Regex.notMatches(input: String) = !this.matches(input)