package com.trade.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trade.store.model.Trade;
import com.trade.store.model.TradeAudit;
import com.trade.store.repository.TradeAuditRepository;
import com.trade.store.repository.TradeRepository;

class TradeServiceTest {

	@Mock
	private TradeRepository tradeRepository;

	@Mock
	private TradeAuditRepository tradeAuditRepository;

	@InjectMocks
	private TradeService tradeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRejectTradeWithPastMaturityDate() {
		Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(1), LocalDate.now(), false);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> tradeService.processTrade(trade));
		assertEquals("Trade maturity date has passed!", exception.getMessage());
	}

	@Test
	void testRejectLowerVersionTrade() {
		Trade existingTrade = new Trade("T1", 2, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		when(tradeRepository.findByTradeId("T1")).thenReturn(Optional.of(existingTrade));
		when(tradeRepository.save(any())).thenReturn(Optional.empty());
		when(tradeAuditRepository.save(any())).thenReturn(Optional.empty());

		Trade lowerVersionTrade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(),
				false);
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> tradeService.processTrade(lowerVersionTrade));
		assertEquals("Rejected lower version trade!", exception.getMessage());
	}

	@Test
	void testReplaceSameVersionTrade() {
		Trade existingTrade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		
		TradeAudit tradeAudit = new TradeAudit(existingTrade.getTradeId() + existingTrade.getVersion(), 
				existingTrade.getTradeId(),
				existingTrade.getVersion(), 
				existingTrade.getCounterPartyId(), 
				existingTrade.getBookId(), 
				existingTrade.getMaturityDate(),
				LocalDate.now(), 
				"CREATED");
		
		Trade sameVersionTrade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now(), false);
		
		when(tradeRepository.findByTradeId("T1")).thenReturn(Optional.of(sameVersionTrade));
		when(tradeRepository.save(existingTrade)).thenReturn(existingTrade);
		when(tradeAuditRepository.save(tradeAudit)).thenReturn(tradeAudit);
		
		tradeService.processTrade(existingTrade);
		verify(tradeRepository, times(1)).save(sameVersionTrade);
	}

	@Test
	void testSaveNewTrade() {
		Trade newTrade = new Trade("T2", 1, "CP-2", "B2", LocalDate.now().plusDays(10), LocalDate.now(), false);

		TradeAudit tradeAudit = new TradeAudit(newTrade.getTradeId() + newTrade.getVersion(), 
				newTrade.getTradeId(),
				newTrade.getVersion(), 
				newTrade.getCounterPartyId(), 
				newTrade.getBookId(), 
				newTrade.getMaturityDate(),
				LocalDate.now(), 
				"CREATED");

		when(tradeRepository.findByTradeId("T2")).thenReturn(Optional.of(newTrade));
		when(tradeRepository.save(newTrade)).thenReturn(newTrade);
		when(tradeAuditRepository.save(tradeAudit)).thenReturn(tradeAudit);

		tradeService.processTrade(newTrade);
		verify(tradeRepository, times(1)).save(newTrade);
	}

}
