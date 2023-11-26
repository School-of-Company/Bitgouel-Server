package team.msg.common.listener

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component
import team.msg.common.logger.LoggerDelegator


/**
 * 현재 익명 클래스를 사용해서 step에서 Listener를 구현중
 * 그러나 향후 사용될 수 있기 때문에 일단은 남겨두겠음
 */
@Component
class BatchStepExecutionListener : StepExecutionListener {

    private val log by LoggerDelegator()

    override fun beforeStep(stepExecution: StepExecution) {
        log.info("Before Step of JobExecutionListener")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        log.info("After Step of JobExecutionListener")

        if(stepExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
            log.error("JobExecutionListener FAILED!!")
            return ExitStatus.FAILED
        }

        return ExitStatus.COMPLETED
    }
}