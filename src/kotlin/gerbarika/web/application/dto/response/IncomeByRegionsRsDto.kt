package gerbarika.web.application.dto.response

import java.time.LocalDateTime

class IncomeByRegionsRsDto(
    var name: String?,
    var sum: Int = 0,
    var number: Int,
    val after: LocalDateTime?,
    val before: LocalDateTime?,
)