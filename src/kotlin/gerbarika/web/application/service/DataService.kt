package gerbarika.web.application.service

import gerbarika.web.application.dto.request.SortPurchaseRqDto
import gerbarika.web.application.dto.response.IncomeDataByParamRsDto

interface DataService {

    fun getIncomeDataByDays(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto>

    fun getIncomeDataByMonths(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto>

    fun getSellingDataByDays(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto>

    fun getSellingDataByMonths(sortPurchaseRqDto: SortPurchaseRqDto): List<IncomeDataByParamRsDto>

}