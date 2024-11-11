package gerbarika.web.application.dto.request

import java.time.LocalDateTime


class SortPurchaseRqDto(
    val after: LocalDateTime,
    val before: LocalDateTime,
    val marketplaceCode: List<String>,
    val innerId: String = ""
)
