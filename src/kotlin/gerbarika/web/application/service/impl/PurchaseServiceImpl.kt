package gerbarika.web.application.service.impl

import gerbarika.web.application.common.Constants
import gerbarika.web.application.common.Constants.Companion.CANCELED_STATUS
import gerbarika.web.application.common.Constants.Companion.OZON
import gerbarika.web.application.common.Constants.Companion.WILDBERRIES
import gerbarika.web.application.common.Constants.Companion.YANDEX
import gerbarika.web.application.dao.entity.Purchase
import gerbarika.web.application.dao.repository.PurchaseRepository
import gerbarika.web.application.dto.PurchaseDto
import gerbarika.web.application.dto.request.SortPurchaseRqDto
import gerbarika.web.application.dto.response.IncomeByGoodsRsDto
import gerbarika.web.application.dto.response.IncomeByRegionsRsDto
import gerbarika.web.application.dto.response.IncomeInfoByRegionNameRsDto
import gerbarika.web.application.mapper.Mapper
import gerbarika.web.application.monitoring.Monitoring
import gerbarika.web.application.service.PurchaseService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class PurchaseServiceImpl(
    private val purchaseRepository: PurchaseRepository,
    private val mapper: Mapper<Purchase, PurchaseDto>
) : PurchaseService {
    @Monitoring("СПИСОК_ЗАКАЗОВ")
    override fun getPurchasesByDate(page: Int): List<PurchaseDto> {
        val pageRequest = PageRequest.of(page, Constants.PURCHASE_PAGE_NUMBER, Sort.by(Sort.Direction.DESC, "date"))
        return purchaseRepository.findAll(pageRequest).content
            .stream()
            .filter { CANCELED_STATUS != it.status }
            .map { purchase ->
                mapper.toDto(purchase)
            }
            .toList()
    }

    @Monitoring("ВЫРУЧКА_ПО_ТОВАРАМ")
    override fun getIncomeByGoods(sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByGoodsRsDto> {
        val goodsMap = purchaseRepository.findAllByDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqD.after,
            sortPurchaseRqD.before,
            sortPurchaseRqD.marketplaceCode
        )
            .stream()
            .filter { CANCELED_STATUS != it.status }
            .collect(
                Collectors.toMap(
                    { it.innerId },
                    {
                        it.price?.toInt()?.let { it1 ->
                            IncomeByGoodsRsDto(
                                it.name,
                                it1,
                                it.innerId,
                                it.wildberriesId,
                                it.yandexId,
                                it.ozonId,
                                it.websiteId,
                                1
                            )
                        }
                    },
                    { element1, element2 ->
                        IncomeByGoodsRsDto(
                            element1.name,
                            element1.sum + element2.sum,
                            element1.innerId,
                            element1.wildberriesId,
                            element1.yandexId,
                            element1.ozonId,
                            element1.websiteId,
                            element1.number + element2.number
                        )
                    }
                )
            ).toMap().values.stream()
            .sorted(Comparator.comparingInt<IncomeByGoodsRsDto?> { it.number }.reversed())
            .toList()
        return goodsMap
    }

    @Monitoring("ВЫРУЧКА_ПО_РЕГИОНАМ")
    override fun getIncomeByRegions(sortPurchaseRqD: SortPurchaseRqDto): List<IncomeByRegionsRsDto> {
        val goodsMap = purchaseRepository.findAllByDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqD.after,
            sortPurchaseRqD.before,
            sortPurchaseRqD.marketplaceCode
        )
            .stream()
            .filter { CANCELED_STATUS != it.status }
            .collect(
                Collectors.toMap(
                    { it.region },
                    {
                        it.price?.let { it1 ->
                            IncomeByRegionsRsDto(
                                it.region,
                                it1.toInt(),
                                1,
                                sortPurchaseRqD.after,
                                sortPurchaseRqD.before
                            )
                        }
                    },
                    { element1, element2 ->
                        IncomeByRegionsRsDto(
                            element1.name,
                            element1.sum + element2.sum,
                            element1.number + element2.number,
                            element1.after,
                            element1.before
                        )
                    }
                )
            ).toMap().values.stream()
            .sorted(Comparator.comparingInt<IncomeByRegionsRsDto?> { it.number }.reversed())
            .toList()
        return goodsMap
    }

    @Monitoring("ИНФОРМАЦИЯ_О_РЕГИОНЕ")
    override fun getIncomeInfoByRegionName(
        after: LocalDateTime,
        before: LocalDateTime,
        region: String
    ): List<IncomeInfoByRegionNameRsDto> {

        var goods = purchaseRepository.findAllByDateAfterAndDateBeforeAndRegion(after, before, region).stream()
            .filter { CANCELED_STATUS != it.status }
            .collect(
                Collectors.toMap(
                    { it.innerId },
                    {
                        val wildberriesPurchases = if (it.marketplaceCode == WILDBERRIES) 1 else 0
                        val yandexPurchases = if (it.marketplaceCode == YANDEX) 1 else 0
                        val ozonPurchases = if (it.marketplaceCode == OZON) 1 else 0
                        it.price?.let { it1 ->
                            IncomeInfoByRegionNameRsDto(
                                it.name,
                                it1.toInt(),
                                it.innerId,
                                it.wildberriesId,
                                it.yandexId,
                                it.ozonId,
                                it.websiteId,
                                wildberriesPurchases,
                                yandexPurchases,
                                ozonPurchases,
                                1
                            )
                        }
                    },
                    { element1, element2 ->
                        IncomeInfoByRegionNameRsDto(
                            element1.name,
                            element1.sum + element2.sum,
                            element1.innerId,
                            element1.wildberriesId,
                            element1.yandexId,
                            element1.ozonId,
                            element1.websiteId,
                            element1.wildberriesPurchases + element2.wildberriesPurchases,
                            element1.yandexPurchases + element2.yandexPurchases,
                            element1.ozonPurchases + element2.ozonPurchases,
                            element1.number + element2.number
                        )
                    }
                )
            ).toMap().values.stream()
            .sorted(Comparator.comparingInt<IncomeInfoByRegionNameRsDto?> { it.number }.reversed())
            .toList()

        return goods;
    }

    @Monitoring("ПОИСК_ЗАКАЗОВ")
    override fun getPurchasesByDatesAndMarketplaceCodes(sortPurchaseRqD: SortPurchaseRqDto): List<PurchaseDto> {
        return purchaseRepository.findAllByDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqD.after,
            sortPurchaseRqD.before,
            sortPurchaseRqD.marketplaceCode
        ).stream()
            .filter { CANCELED_STATUS != it.status }
            .sorted(Comparator.comparing(Purchase::date).reversed())
            .map { purchase ->
                mapper.toDto(purchase)
            }
            .toList();
    }
}
