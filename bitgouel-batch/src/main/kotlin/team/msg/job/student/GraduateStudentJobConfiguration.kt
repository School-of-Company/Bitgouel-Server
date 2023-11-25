package team.msg.job.student

import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.msg.common.util.ParamUtil

/**
 * parameter로 넘어온 period의 student를 graduate로 갱신하는 Job
 * > 첫 실행 2024. 03. 02
 * period 설정 기준 현재년도 - 2023
 */
@Configuration
class GraduateStudentJobConfiguration(
    private val parameter: GraduateJobParameter
) {

    companion object {
        const val CHUNK_SIZE: Int = 50 // 변경될 수 있음
        const val JOB_NAME: String = "graduate_student_job"
        const val PARAMETER: String = JOB_NAME + "_parameter"
    }

    /**
     * GraduateStudentJobParamter 실행 커맨드 예시
     * --JOB_NAME=graduate_student_job VERSION=1 DATE_TIME=2024/03/02-00:00:00-KST
     */
    @Bean(PARAMETER)
    @JobScope
    fun parameter(
        @Value("#{jobParameters[VERSION]}") version: Int,
        @Value("#{jobParameters[DATE_TIME]}") startDate: String,
        @Value("#{jobParameters[PERIOD]}") period: Int
    ) : GraduateJobParameter {
        return GraduateJobParameter(
            version = version,
            jobStartDate = ParamUtil.strDateTimeToLocalDateTime(startDate),
            period = period
        )
    }


}