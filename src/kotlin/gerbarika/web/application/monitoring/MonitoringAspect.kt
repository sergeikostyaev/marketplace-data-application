package gerbarika.web.application.monitoring

import gerbarika.web.application.monitoring.processor.MonitoringProcessor
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Aspect
@Component
class MonitoringAspect(private val monitoringProcessor: MonitoringProcessor) {

    private val NOT_MONITORABLE_USER: String = "admin"

    @Around("@annotation(monitoring)")
    fun handleMonitoring(proceedingJoinPoint: ProceedingJoinPoint, monitoring: Monitoring): Any? {

        val user = SecurityContextHolder.getContext().authentication.name

        val metricName: String = monitoring.value
        var result: Any? = null
        val time = System.currentTimeMillis()

        if (!user.equals(NOT_MONITORABLE_USER)) {
            monitoringProcessor.notifyTotal(metricName)
        }

        result = try {
            proceedingJoinPoint.proceed()
        } catch (e: Throwable) {
            monitoringProcessor.notifyError(metricName)
            throw RuntimeException(e)
        }

        if (!user.equals(NOT_MONITORABLE_USER)) {
            monitoringProcessor.notifySuccess(metricName, System.currentTimeMillis() - time)
        }
        return result
    }
}
