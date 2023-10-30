package team.msg.domain.health

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController(
    private val env: Environment
) {

    @GetMapping
    fun healthCheck() = "Bitgouel Server is OK, PORT = ${env.getProperty("server.port")}"

}