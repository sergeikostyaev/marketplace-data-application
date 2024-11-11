package gerbarika.web.application.dao.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "purchases")
class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "inner_id")
    val innerId: String? = ""

    @Column(name = "wildberries_id")
    val wildberriesId: String? = ""

    @Column(name = "yandex_id")
    val yandexId: String? = ""

    @Column(name = "ozon_id")
    val ozonId: String? = ""

    @Column(name = "website_id")
    val websiteId: String? = ""

    @Column(name = "marketplace_code")
    val marketplaceCode: String? = ""

    @Column(name = "status")
    val status: String? = ""

    @Column(name = "purchase_name")
    val name: String? = ""

    @Column(name = "purchase_date")
    val date: LocalDateTime? = LocalDateTime.now()

    @Column(name = "region")
    val region: String? = ""

    @Column(name = "price")
    val price: String? = ""

}