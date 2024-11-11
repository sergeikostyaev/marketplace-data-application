package gerbarika.web.application.monitoring.processor


interface MonitoringProcessor {
    fun notifyTotal(metricName: String?)
    fun notifySuccess(metricName: String?, duration: Long)
    fun notifyError(metricName: String?)
}
