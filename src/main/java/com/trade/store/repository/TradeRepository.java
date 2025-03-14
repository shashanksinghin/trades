package com.trade.store.repository;

import com.trade.store.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	List<Trade> findByTradeId(String tradeId);
}
