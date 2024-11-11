package gerbarika.web.application.dao.repository

import gerbarika.web.application.dao.entity.Purchase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface PurchaseRepository : JpaRepository<Purchase, Long?> {


    override fun findAll(pageable: Pageable): Page<Purchase>

    @Query("SELECT e FROM Purchase e WHERE e.marketplaceCode IN :marketplaceCodes AND e.date >= :after AND e.date <= :before ORDER BY e.date")
    fun findAllByDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
        after: LocalDateTime,
        before: LocalDateTime,
        @Param("marketplaceCodes") marketplaceCodes: List<String>
    ): List<Purchase>

    @Query("SELECT e FROM Purchase e WHERE e.marketplaceCode IN :marketplaceCodes AND e.innerId = :innerId " +
            "AND e.date >= :after AND e.date <= :before ORDER BY e.date")
    fun findAllByInnerIdAndDateAfterAndDateBeforeAndMarketplaceCodeOrderByDate(
        innerId: String,
        after: LocalDateTime,
        before: LocalDateTime,
        @Param("marketplaceCodes") marketplaceCodes: List<String>
    ): List<Purchase>

    fun findAllByDateAfterAndDateBeforeAndRegion(
        after: LocalDateTime,
        before: LocalDateTime, region: String
    ): List<Purchase>
}


