package team.msg.common.parameter

/**
 * 개발/QA 도중에 같은 Job 파라미터 값으로 배치 실행이 가능하도록 version을 지정합니다.
 * version은 1부터 시작합니다.
 */
open class BaseJobParameter(open val version: Int)