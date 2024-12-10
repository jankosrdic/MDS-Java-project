package com.mycompany.mds.model.service;

import com.mycompany.mds.model.service.ProfitAnalyzer;
import com.mycompany.mds.model.service.ProfitAnalyzer.ProfitResult;
import com.mycompany.mds.model.database.entity.StockPrice;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfitAnalyzerTest {

	@Test
	void testCalculateMaxProfit() {
		ProfitAnalyzer profitAnalyzer = new ProfitAnalyzer();
		List<StockPrice> prices = List.of(
			new StockPrice("AAPL", LocalDate.of(2022, 1, 1), 100, 110, 90, 105, 105, 1000),
			new StockPrice("AAPL", LocalDate.of(2022, 1, 2), 105, 115, 95, 110, 110, 2000),
			new StockPrice("AAPL", LocalDate.of(2022, 1, 3), 110, 120, 100, 115, 115, 1500)
		);

		ProfitResult result = profitAnalyzer.calculateMaxProfit(prices);

		assertEquals(LocalDate.of(2022, 1, 1), result.getBuyDate());
		assertEquals(LocalDate.of(2022, 1, 3), result.getSellDate());
		assertEquals(105, result.getBuyPrice()); 
		assertEquals(115, result.getSellPrice());
		assertEquals(10, result.getMaxProfit());
	}

	@Test
	void testCalculateMaxProfit_EmptyList() {
		ProfitAnalyzer profitAnalyzer = new ProfitAnalyzer();

		ProfitResult result = profitAnalyzer.calculateMaxProfit(List.of());

		assertNull(result.getBuyDate());
		assertNull(result.getSellDate());
		assertEquals(0, result.getBuyPrice());
		assertEquals(0, result.getSellPrice());
		assertEquals(0, result.getMaxProfit());
	}
}
