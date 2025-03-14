package com.trade.store.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.trade.store.model.TradeAudit;

public interface TradeAuditRepository extends MongoRepository<TradeAudit, Long> {
    List<TradeAudit> findByTradeId(String tradeId);
}
