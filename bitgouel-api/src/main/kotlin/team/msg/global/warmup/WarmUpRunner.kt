package team.msg.global.warmup

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import team.msg.thirdparty.feign.client.WarmUpClient

@Component
class WarmUpRunner(
    private val warmUpClient: WarmUpClient
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        runCatching {
            warmUpClient.queryFaq()
        }.onFailure {  }
    }
}