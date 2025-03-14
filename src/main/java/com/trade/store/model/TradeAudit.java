package com.trade.store.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "trade_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeAudit {
	@Id
	private String id; // tradeId + "_" + version

	private String tradeId;

	private Integer version;

	private String counterPartyId;

	private String bookId;

	private LocalDate maturityDate;

	private LocalDate createdDate = LocalDate.now();

	private String action; // "CREATED", "UPDATED", "REJECTED"
}
