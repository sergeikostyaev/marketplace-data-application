package gerbarika.web.application.service

import gerbarika.web.application.dto.PurchaseDto
import gerbarika.web.application.dto.request.SortPurchaseRqDto
import gerbarika.web.application.dto.response.IncomeByGoodsRsDto
import gerbarika.web.application.dto.response.IncomeByRegionsRsDto
import gerbarika.web.application.dto.response.IncomeInfoByRegionNameRsDto
import java.time.LocalDateTime

interface PurchaseService {

    fun getPurchasesByDate(page: Int): List<PurchaseDto>

    fun getIncomeByGoods(sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByGoodsRsDto>

    fun getIncomeByRegions(sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByRegionsRsDto>

    fun getIncomeInfoByRegionName(
        after: LocalDateTime,
        before: LocalDateTime, region: String
    ): List<IncomeInfoByRegionNameRsDto>

    fun getPurchasesByDatesAndMarketplaceCodes(sortPurchaseRqD: SortPurchaseRqDto): List<PurchaseDto>

}