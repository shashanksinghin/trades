package com.trade.store.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	@Id
	@NotNull
	private String tradeId;

	@NotNull
	private Integer version;

	@NotNull
	private String counterPartyId;

	@NotNull
	private String bookId;

	@NotNull
	@FutureOrPresent(message = "Maturity date must be today or in the future")
	private LocalDate maturityDate;

	private LocalDate createdDate = LocalDate.now();

	private Boolean expired = false;
}
