package team.msg.global.warmup

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import team.msg.thirdparty.feign.club.client.ClubClient
import team.msg.thirdparty.feign.company.client.CompanyClient
import team.msg.thirdparty.feign.faq.client.FaqClient
import team.msg.thirdparty.feign.government.client.GovernmentClient

@Component
class WarmUpRunner(
    private val faqClient: FaqClient,
    private val clubClient: ClubClient,
    private val companyClient: CompanyClient,
    private val governmentClient: GovernmentClient
): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        runCatching {
            faqClient.queryFaq()
            clubClient.queryAllClubNames(null)
            companyClient.queryCompanies()
            governmentClient.queryGovernments()
        }.onFailure {  }
    }
}