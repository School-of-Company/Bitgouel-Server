package team.msg.job.student

import team.msg.common.parameter.BaseJobParameter
import java.time.LocalDateTime

class GraduateJobParameter(
    override val version: Int,
    val jobStartDate: LocalDateTime,
    val period: Int
) : BaseJobParameter(version)
