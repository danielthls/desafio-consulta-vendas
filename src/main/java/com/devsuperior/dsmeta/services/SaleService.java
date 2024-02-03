package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleSummaryDTO>getSummary(String minDate, String maxDate,
										  String name, Pageable pageable) {
		LocalDate minLocalDate = setMinDate(minDate);
		LocalDate maxLocalDate = setMaxDate(maxDate);
		return repository.getSummary(minLocalDate, maxLocalDate, name, pageable);
	}

	public Page<SaleReportDTO> getReport(String minDate, String maxDate,
										 String name, Pageable pageable) {
		LocalDate minLocalDate = setMinDate(minDate);
		LocalDate maxLocalDate = setMaxDate(maxDate);
		return repository.getReport(minLocalDate, maxLocalDate, name, pageable)
				.map(x -> new SaleReportDTO(x));
	}

	private LocalDate setMinDate(String minDate) {
		LocalDate minLocalDate = LocalDate.ofInstant(Instant.now(),
				ZoneId.systemDefault()).minusYears(1L);
		if (!minDate.isEmpty()) minLocalDate = LocalDate.parse(minDate,
				DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return minLocalDate;
	}

	private LocalDate setMaxDate(String maxDate) {
		LocalDate maxLocalDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		if (!maxDate.isEmpty()) maxLocalDate = LocalDate.parse(maxDate,
				DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return maxLocalDate;
	}
}
