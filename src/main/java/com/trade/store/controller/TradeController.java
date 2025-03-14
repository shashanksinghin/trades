package com.trade.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trade.store.model.Trade;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final KafkaTemplate<String, Trade> kafkaTemplate;

    public TradeController(KafkaTemplate<String, Trade> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> sendTrade(@RequestBody Trade trade) {
        kafkaTemplate.send("trade", trade);
        return ResponseEntity.ok("Trade sent for processing.");
    }
}
	