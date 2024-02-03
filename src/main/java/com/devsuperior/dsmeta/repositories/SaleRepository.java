package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(se.name, sum(sa.amount)) " +
            "        FROM Sale sa " +
            "        JOIN sa.seller se " +
            "        WHERE sa.date BETWEEN :minDate AND :maxDate AND " +
            "        UPPER(se.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "        GROUP BY se.name   " )
    Page<SaleSummaryDTO> getSummary(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);


    @Query(value = "SELECT obj " +
            "FROM Sale obj JOIN FETCH obj.seller " +
            "WHERE obj.date BETWEEN :minDate AND :maxDate AND " +
            "UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))",
            countQuery = "SELECT COUNT(obj) FROM Sale obj JOIN obj.seller")
    Page<Sale> getReport(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);
}
