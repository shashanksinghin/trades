package com.trade.store.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.store.model.TradeAudit;

public interface TradeAuditRepository extends MongoRepository<TradeAudit, Long> {
	Optional<TradeAudit> findByTradeId(String tradeId);
}
