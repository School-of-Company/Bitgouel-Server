package bitgouel.team.msg.global.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration

@Configuration
@EntityScan("domain")
class EntityScanConfig