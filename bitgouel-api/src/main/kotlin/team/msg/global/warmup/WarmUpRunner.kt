package team.msg.global.warmup

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import team.msg.thirdparty.feign.faq.client.FaqClient

@Component
class WarmUpRunner(
    private val faqClient: FaqClient
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        runCatching {
            faqClient.queryFaq()
        }.onFailure {  }
    }
}