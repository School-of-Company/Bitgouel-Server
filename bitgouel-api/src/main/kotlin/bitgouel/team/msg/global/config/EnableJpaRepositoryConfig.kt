package bitgouel.team.msg.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("domain")
class EnableJpaRepositoryConfig {
}