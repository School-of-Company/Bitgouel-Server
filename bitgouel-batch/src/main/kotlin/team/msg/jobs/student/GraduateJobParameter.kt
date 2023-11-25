package team.msg.jobs.student

import team.msg.common.parameter.BaseJobParameter
import java.time.LocalDateTime

class GraduateJobParameter(
    override val version: Int,
    val jobStartDate: LocalDateTime,
    val cohort: Int
) : BaseJobParameter(version)
