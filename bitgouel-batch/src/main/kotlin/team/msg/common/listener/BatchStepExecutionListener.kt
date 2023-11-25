package team.msg.common.listener

import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component
import team.msg.common.logger.LoggerDelegator

@Component
class BatchStepExecutionListener : StepExecutionListener {

    private val log by LoggerDelegator()

    override fun beforeStep(stepExecution: StepExecution) {
        log.info("Before Step of JobExecutionListener")
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        log.info("After Step of JobExecutionListener")

        if(stepExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
            log.error("payClearStep FAILED!!")
            return ExitStatus.FAILED
        }

        return ExitStatus.COMPLETED
    }
}