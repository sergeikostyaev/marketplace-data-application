package gerbarika.web.application.service.impl

import gerbarika.web.application.common.Constants.Companion.CANCELED_STATUS
import gerbarika.web.application.dao.entity.Purchase
import gerbarika.web.application.dao.repository.PurchaseRepository
import gerbarika.web.application.dto.request.SortPurchaseRqDto
import gerbarika.web.application.dto.response.IncomeDataByParamRsDto
import gerbarika.web.application.monitoring.Monitoring
import gerbarika.web.application.service.DataService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Objects.nonNull
import java.util.stream.Collectors

@Service
class DataServiceImpl(private val purchaseRepository: PurchaseRepository) : DataService {
    @Monitoring("ГРАФИК_ОБЩЕЙ_ВЫРУЧКИ")
    override fun getIncomeDataByDays(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto> {

        val purchases = getFilteredPurchases(sortPurchaseRqDto)

        val map = processPurchasesToResponse(
            purchases,
            { it.date?.toLocalDate() },
            { it.date?.toLocalDate().toString() },
            { it.price?.toInt() ?: 0 }
        )

        return insertEmptyDates(map, sortPurchaseRqDto)
    }

    @Monitoring("ГРАФИК_ОБЩЕЙ_ВЫРУЧКИ")
    override fun getIncomeDataByMonths(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto> {

        val purchases = getFilteredPurchases(sortPurchaseRqDto)

        return processPurchasesToResponse(
            purchases,
            { it.date?.toLocalDate()?.month.toString().plus("-".plus(it.date?.toLocalDate()?.year)) },
            { it.date?.toLocalDate()?.month?.name.plus("-".plus(it.date?.toLocalDate()?.year)) },
            { it.price?.toInt() ?: 0 }
        ).values.stream().sorted(Comparator.comparing {
            Month.valueOf(
                it.name.toString().substring(0, it.name.toString().indexOf('-'))
            ).ordinal
        })
            .toList()
    }

    @Monitoring("ГРАФИК_ПРОДАЖ")
    override fun getSellingDataByDays(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto> {
        val purchases = purchaseRepository.findAllByInnerIdAndDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqDto.innerId,
            sortPurchaseRqDto.after,
            sortPurchaseRqDto.before,
            sortPurchaseRqDto.marketplaceCode
        )

        val map = processPurchasesToResponse(
            purchases, {it.date?.toLocalDate()},
            {it.date?.toLocalDate().toString()},
            {1}
        )

        return insertEmptyDates(map, sortPurchaseRqDto)
    }

    @Monitoring("ГРАФИК_ПРОДАЖ")
    override fun getSellingDataByMonths(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto> {
        val purchases = purchaseRepository.findAllByInnerIdAndDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqDto.innerId,
            sortPurchaseRqDto.after,
            sortPurchaseRqDto.before,
            sortPurchaseRqDto.marketplaceCode
        )

        return processPurchasesToResponse(
            purchases,
            { it.date?.toLocalDate()?.month.toString().plus("-".plus(it.date?.toLocalDate()?.year)) },
            { it.date?.toLocalDate()?.month?.name.plus("-".plus(it.date?.toLocalDate()?.year)) },
            {1}
        ).values.stream().sorted(Comparator.comparing {
            Month.valueOf(
                it.name.toString().substring(0, it.name.toString().indexOf('-'))
            ).ordinal
        })
            .toList()
    }

    private fun <T> processPurchasesToResponse(
        purchases: List<Purchase>,
        dateExtractor: (Purchase) -> T,
        nameExtractor: (Purchase) -> String,
        sumExtractor: (Purchase) -> Int,
    ): MutableMap<T, IncomeDataByParamRsDto> {
        return purchases.stream()
            .collect(
                Collectors.toMap(
                    dateExtractor,
                    { IncomeDataByParamRsDto(nameExtractor(it), sumExtractor(it)) },
                    { element1, element2 ->
                        IncomeDataByParamRsDto(element1.name, element1.sum + element2.sum)
                    }
                )
            ).toMutableMap()
    }

    private fun getFilteredPurchases(sortPurchaseRqDto: SortPurchaseRqDto) = purchaseRepository
        .findAllByDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
            sortPurchaseRqDto.after, sortPurchaseRqDto.before, sortPurchaseRqDto.marketplaceCode
        )
        .filter { CANCELED_STATUS != it.status && nonNull(it.price) && it.price?.matches(Regex("\\d+"))!! }

    private fun insertEmptyDates(
        map: MutableMap<LocalDate?, IncomeDataByParamRsDto>,
        sortPurchaseRqD: SortPurchaseRqDto
    ): List<IncomeDataByParamRsDto> {

        var counter: LocalDate = sortPurchaseRqD.after.toLocalDate()

        while (!counter.isAfter(sortPurchaseRqD.before.toLocalDate())) {
            if (!map.keys.contains(counter)) {
                map[counter] = IncomeDataByParamRsDto(
                    counter.toString(),
                    0
                )
            }
            counter = counter.plusDays(1)
        }

        return map.values.stream()
            .sorted(Comparator.comparing { LocalDate.parse(it.name) })
            .map {
                IncomeDataByParamRsDto(
                    LocalDate.parse(it.name, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")), it.sum
                )
            }
            .toList()
    }
}