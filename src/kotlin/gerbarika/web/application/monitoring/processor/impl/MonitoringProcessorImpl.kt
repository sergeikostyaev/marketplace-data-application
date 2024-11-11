package gerbarika.web.application.monitoring.processor.impl

import gerbarika.web.application.monitoring.processor.MonitoringProcessor
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.logging.Logger
@Component
class MonitoringProcessorImpl(private val meterRegistry: MeterRegistry) : MonitoringProcessor {

    companion object {
        val log: Logger = Logger.getLogger(MonitoringProcessorImpl::class.java.name)
    }


    private val COUNTER_TOTAL_PATTERN = "TOTAL_%s"
    private val COUNTER_SUCCESS_PATTERN = "SUCCESS_%s"
    private val COUNTER_ERROR_PATTERN = "ERROR_%s"
    private val TIMER_PATTERN = "TIMER_%s"


    override fun notifyTotal(metricName: String?) {
        notifyCounter(String.format(COUNTER_TOTAL_PATTERN, metricName))
    }

    override fun notifySuccess(metricName: String?, duration: Long) {
        notifyCounter(String.format(COUNTER_SUCCESS_PATTERN, metricName))
        notifyTimer(String.format(TIMER_PATTERN, metricName), duration)
    }

    override fun notifyError(metricName: String?) {
        notifyCounter(String.format(COUNTER_ERROR_PATTERN, metricName))
    }

    private fun notifyCounter(metricName: String) {
        try {
            meterRegistry.counter(metricName).increment()
        } catch (e: Exception) {
            log.info("notifyCounter error: " + e.message)
        }
    }

    private fun notifyTimer(metricName: String, duration: Long) {
        try {
            meterRegistry.timer(metricName).record(Duration.ofMillis(duration))
        } catch (e: Exception) {
            log.info("notifyTimer error: " + e.message)
        }
    }
}