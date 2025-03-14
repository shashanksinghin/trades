package com.trade.store.kafka;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.model.Trade;
import com.trade.store.service.TradeService;


class TradeConsumerTest {

	@Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeConsumer tradeConsumer;

    public TradeConsumerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeTrade() {
        Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
        tradeConsumer.consumeTrade(trade);
        verify(tradeService, times(1)).processTrade(trade);
    }


}
