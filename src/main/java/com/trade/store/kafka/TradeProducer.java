package com.trade.store.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.trade.store.model.Trade;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeProducer {
	private final KafkaTemplate<String, Trade> kafkaTemplate;
	private static final String TOPIC = "trade-store-topic";

	public void sendTrade(Trade trade) {
		kafkaTemplate.send(TOPIC, trade);
	}
}
