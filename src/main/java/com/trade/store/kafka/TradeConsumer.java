package com.trade.store.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.trade.store.model.Trade;
import com.trade.store.service.TradeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeConsumer {

	private final TradeService tradeService;

	@KafkaListener(topics = "trade", groupId = "trade-store-group")
	public void consumeTrade(Trade trade) {
		tradeService.processTrade(trade);
	}
}
