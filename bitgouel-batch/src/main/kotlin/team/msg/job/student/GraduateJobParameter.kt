package team.msg.job.student

import team.msg.common.BaseJobParameter

class GraduateJobParameter(
    override val version: Int,
    val period: Int
) : BaseJobParameter(version)
