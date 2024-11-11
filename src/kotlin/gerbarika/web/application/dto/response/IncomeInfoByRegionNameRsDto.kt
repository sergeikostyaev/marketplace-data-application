package gerbarika.web.application.dto.response

class IncomeInfoByRegionNameRsDto(
    var name: String?,
    var sum: Int = 0,
    var innerId: String?,
    var wildberriesId: String?,
    var yandexId: String?,
    var ozonId: String?,
    var websiteId: String?,
    var wildberriesPurchases: Int,
    var yandexPurchases: Int,
    var ozonPurchases: Int,
    var number: Int
)