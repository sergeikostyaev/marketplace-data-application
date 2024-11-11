package gerbarika.web.application.mapper.impl

import gerbarika.web.application.dao.entity.Purchase
import gerbarika.web.application.dto.PurchaseDto
import gerbarika.web.application.mapper.Mapper
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class PurchaseMapper : Mapper<Purchase, PurchaseDto> {
    override fun toDto(purchase: Purchase): PurchaseDto {
        return PurchaseDto(purchase.id, purchase.innerId, purchase.wildberriesId, purchase.yandexId, purchase.ozonId, purchase.websiteId, purchase.marketplaceCode,
            purchase.name, LocalDateTime.parse(purchase.date.toString(), DateTimeFormatter.ISO_DATE_TIME)
                .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")),
            purchase.region, purchase.price
        )
    }

}