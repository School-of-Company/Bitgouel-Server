package team.msg.jobs.student

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import team.msg.common.listener.BatchStepExecutionListener
import team.msg.common.logger.LoggerDelegator
import team.msg.common.util.ParamUtil
import team.msg.domain.student.enums.StudentRole
import team.msg.domain.student.model.Student

/**
 * parameter로 넘어온 period의 student를 graduate로 갱신하는 Job
 * > 첫 실행 2024. 03. 02
 * period 설정 기준 현재년도 - 2023
 */
@Configuration
class GraduateStudentJobConfiguration(
    private val parameter: GraduateJobParameter,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val batchStepExecutionListener: BatchStepExecutionListener,
    private val dataSource: DataSource,
    private val entityManagerFactory: EntityManagerFactory
) {

    private val log by LoggerDelegator()

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
        @Value("#{jobParameters[COHORT]}") cohort: Int
    ) : GraduateJobParameter {
        return GraduateJobParameter(
            version = version,
            jobStartDate = ParamUtil.strDateTimeToLocalDateTime(startDate),
            cohort = cohort
        )
    }

    @Bean
    fun graduateStudentJob(): Job {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart() // 재시작 false
            .start(graduateStudentStep()) // 졸업생 전환 step 시작
            .on("FAILED") // 만약 할당 Step이 실패한다면
            .to(clearStudentStep()) // 초기화 Step
            .on("FAILED") // 초기화 Step 실패시
            .fail() // Job 실패
            .on("*") // 초기화 Step 결과가 FAILED를 제외한 모든 경우엔
            .end() // Flow 종료
            .from(graduateStudentStep()) // 할당 Job으로부터
            .on("*") // FAIL을 제외한 모든 경우엔
            .end() // Flow 종료
            .end() // Job 종료
            .build();
    }

    @Bean
    @JobScope
    fun graduateStudentStep(): Step {
        return stepBuilderFactory.get(JOB_NAME + "_graduate_step")
            .chunk<Student, Student>(CHUNK_SIZE)
            .reader(graduateStudentItemReader())
            .processor(graduateStudentItemProcessor())
            .writer(graduateStudentItemWriter())
            .listener(object: StepExecutionListener {
                override fun beforeStep(stepExecution: StepExecution) {
                    log.info("Before Step of GraduateStudentStep")
                }

                override fun afterStep(stepExecution: StepExecution): ExitStatus {
                    log.info("After Step of GraduateStudentStep")

                    if(stepExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
                        log.error("GraduateStudentStep FAILED!!")
                        return ExitStatus.FAILED
                    }

                    return ExitStatus.COMPLETED
                }
            })
            .build()
    }

    @Bean
    @JobScope
    fun clearStudentStep(): Step {
        return stepBuilderFactory.get(JOB_NAME + "_clear_step")
            .chunk<Student, Student>(CHUNK_SIZE)
            .reader(graduateStudentItemReader())
            .processor(clearStudentItemProcessor())
            .writer(graduateStudentItemWriter())
            .listener(object: StepExecutionListener {
                override fun beforeStep(stepExecution: StepExecution) {
                    log.info("Before Step of ClearStudentStep")
                }

                override fun afterStep(stepExecution: StepExecution): ExitStatus? {
                    log.info("After Step of GraduateStudentStep")

                    if(stepExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
                        log.error("GraduateStudentStep FAILED!!")
                        return ExitStatus.FAILED
                    }

                    return ExitStatus.COMPLETED
                }

            })
            .build()
    }

    @Bean
    @StepScope
    fun graduateStudentItemReader(): JdbcCursorItemReader<Student> {
        logCohort()
        return JdbcCursorItemReaderBuilder<Student>()
            .sql("SELECT s " +
                    "FROM STUDENT s WHERE s.studentRole = STUDENT " +
                    "AND s.cohort = ${parameter.cohort} " +
                    "ORDER BY s.class_room ASC, " +
                    "s.number ASC"
            ).rowMapper(BeanPropertyRowMapper(Student::class.java))
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .name("graduateStudentReader")
            .build()
    }

    @Bean
    @StepScope
    fun graduateStudentItemProcessor(): ItemProcessor<Student, Student> = ItemProcessor { newGraduateStudent(it) }

    @Bean
    @StepScope
    fun clearStudentItemProcessor(): ItemProcessor<Student, Student> = ItemProcessor { clearStudent(it) }

    @Bean
    @StepScope
    fun graduateStudentItemWriter(): JpaItemWriter<Student> {
        val writer = JpaItemWriter<Student>()
        writer.setEntityManagerFactory(entityManagerFactory)
        writer.setUsePersist(false)
        return writer
    }

    private fun newGraduateStudent(student: Student): Student {
        return student.run {
            Student(
                id = id,
                user = user,
                club = club,
                classRoom = classRoom,
                grade = grade,
                number = number,
                cohort = cohort,
                credit = credit,
                studentRole = StudentRole.GRADUATE
            )
        }
    }

    private fun clearStudent(student: Student): Student {
        return student.run {
            Student(
                id = id,
                user = user,
                club = club,
                classRoom = classRoom,
                grade = grade,
                number = number,
                cohort = cohort,
                credit = credit,
                studentRole = StudentRole.STUDENT
            )
        }
    }

    private fun logCohort() {
        val startDateCohort = parameter.jobStartDate.year - 2023
        val cohort = parameter.cohort

        log.info("""
            STUDENT READ를 시작합니다. 시작 날짜: ${parameter.jobStartDate}
            졸업 예정 기수: $startDateCohort, parameter.cohort: $cohort
            parameter.cohort == 졸업 예정 기수: ${startDateCohort == cohort}
            """)
    }

}