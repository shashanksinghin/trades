package com.trade.store.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.trade.store.model.Trade;
import com.trade.store.model.TradeAudit;
import com.trade.store.repository.TradeAuditRepository;
import com.trade.store.repository.TradeRepository;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeAuditRepository tradeAuditRepository;

    public TradeService(TradeRepository tradeRepository, TradeAuditRepository tradeAuditRepository) {
        this.tradeRepository = tradeRepository;
        this.tradeAuditRepository = tradeAuditRepository;
    }

    @KafkaListener(topics = "trade-store-topic", groupId = "trade-group")
    public void processTrade(Trade trade) {
    	Optional<Trade> existingTrades = tradeRepository.findByTradeId(trade.getTradeId());

        existingTrades.stream().max((t1, t2) -> Integer.compare(t1.getVersion(), t2.getVersion()))
                .ifPresent(existingTrade -> {
                    if (trade.getVersion() < existingTrade.getVersion()) {
                        throw new IllegalArgumentException("Trade version is lower than existing record. Rejected.");
                    }
                });

        tradeRepository.save(trade);
        tradeAuditRepository.save(new TradeAudit(trade.getTradeId()+trade.getVersion(),
        		trade.getTradeId(), 
        		trade.getVersion(), 
        		trade.getCounterPartyId(), 
        		trade.getBookId(), 
        		trade.getMaturityDate(), 
        		LocalDate.now(),
        		"CREATED"));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredTrades() {
        tradeRepository.findAll().forEach(trade -> {
            if (trade.getMaturityDate().isBefore(LocalDate.now()) && !trade.getExpired()) {
                trade.setExpired(true);
                tradeRepository.save(trade);
            }
        });
    }
}
