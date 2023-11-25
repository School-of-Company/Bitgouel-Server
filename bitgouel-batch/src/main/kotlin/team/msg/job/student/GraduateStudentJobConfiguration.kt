package team.msg.job.student

import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraduateStudentJobConfiguration(
    private val parameter: GraduateJobParameter
) {

    companion object {
        const val CHUNK_SIZE: Int = 50
        const val JOB_NAME: String = "graduate_student_job"
        const val PARAMETER: String = JOB_NAME + "_parameter"
    }

    @Bean(PARAMETER)
    @JobScope
    fun parameter(
        @Value("#{jobParameters[VERSION]}") version: Int,
        @Value("#{jobParameters[PERIOD]}") period: Int
    ) : GraduateJobParameter {
        return GraduateJobParameter(
            version = version,
            period = period
        )
    }


}