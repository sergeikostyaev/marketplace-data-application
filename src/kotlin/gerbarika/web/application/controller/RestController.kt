package gerbarika.web.application.controller

import gerbarika.web.application.dto.PurchaseDto
import gerbarika.web.application.dto.request.SortPurchaseRqDto
import gerbarika.web.application.dto.response.IncomeByGoodsRsDto
import gerbarika.web.application.dto.response.IncomeByRegionsRsDto
import gerbarika.web.application.dto.response.IncomeDataByParamRsDto
import gerbarika.web.application.dto.response.IncomeInfoByRegionNameRsDto
import gerbarika.web.application.service.DataService
import gerbarika.web.application.service.PurchaseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
class RestController(private val purchaseService: PurchaseService, private val dataService: DataService) {

    @GetMapping("/api/purchase/list")
    fun getPurchasesByDate(@RequestParam page: Int): List<PurchaseDto> = purchaseService.getPurchasesByDate(page)

    @PostMapping("/api/purchase/sorted/income")
    fun getIncomeByGoods(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByGoodsRsDto> =
        purchaseService.getIncomeByGoods(sortPurchaseRqD)

    @PostMapping("/api/purchase/sorted/region")
    fun getIncomeByRegions(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByRegionsRsDto> =
        purchaseService.getIncomeByRegions(sortPurchaseRqD)

    @PostMapping("/api/region/info")
    fun getIncomeInfoByRegionName(
        @RequestParam after: LocalDateTime,
        @RequestParam before: LocalDateTime,
        @RequestParam region: String
    ): List<IncomeInfoByRegionNameRsDto> =
        purchaseService.getIncomeInfoByRegionName(after, before, region)

    @PostMapping("/api/purchase/list/sorted")
    fun getPurchasesByDatesAndMarketplaceCodes(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<PurchaseDto> =
        purchaseService.getPurchasesByDatesAndMarketplaceCodes(sortPurchaseRqD)

    @PostMapping("/api/data/income/day")
    fun getPurchaseDataByIncomeByDays(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeDataByParamRsDto> =
        dataService.getIncomeDataByDays(sortPurchaseRqD)

    @PostMapping("/api/data/income/month")
    fun getPurchaseDataByIncomeByMonths(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeDataByParamRsDto> =
        dataService.getIncomeDataByMonths(sortPurchaseRqD)

    @PostMapping("/api/data/item/day")
    fun getSellingDataByDays(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeDataByParamRsDto> =
        dataService.getSellingDataByDays(sortPurchaseRqD)

    @PostMapping("/api/data/item/month")
    fun getSellingDataByMonths(@RequestBody sortPurchaseRqD: SortPurchaseRqDto): List<IncomeDataByParamRsDto> =
        dataService.getSellingDataByMonths(sortPurchaseRqD)
}

