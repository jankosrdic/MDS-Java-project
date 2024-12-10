package com.mycompany.mds.model.service;

import com.mycompany.mds.model.database.entity.StockPrice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProfitAnalyzer {

	public static class ProfitResult {

		private LocalDate buyDate;
		private LocalDate sellDate;
		private double buyPrice;
		private double sellPrice;
		private double maxProfit;

		public ProfitResult(LocalDate buyDate, LocalDate sellDate, double buyPrice, double sellPrice, double maxProfit) {
			this.buyDate = buyDate;
			this.sellDate = sellDate;
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
			this.maxProfit = maxProfit;
		}

		public LocalDate getBuyDate() {
			return buyDate;
		}

		public LocalDate getSellDate() {
			return sellDate;
		}

		public double getBuyPrice() {
			return buyPrice;
		}

		public double getSellPrice() {
			return sellPrice;
		}

		public double getMaxProfit() {
			return maxProfit;
		}
	}

	public ProfitResult calculateMaxProfit(List<StockPrice> prices) {
		if (prices.isEmpty()) {
			return new ProfitResult(null, null, 0, 0, 0);
		}

		double maxProfit = 0;
		double minPrice = Double.MAX_VALUE;
		LocalDate buyDate = null;
		LocalDate sellDate = null;
		double buyPrice = 0;
		double sellPrice = 0;

		for (int i = 0; i < prices.size(); i++) {
			StockPrice buyPriceCandidate = prices.get(i);

			if (buyPriceCandidate.getClosePrice() < minPrice) {
				minPrice = buyPriceCandidate.getClosePrice();
				buyDate = buyPriceCandidate.getDate();
				buyPrice = buyPriceCandidate.getClosePrice();
			}

			for (int j = i + 1; j < prices.size(); j++) {
				StockPrice sellPriceCandidate = prices.get(j);
				double profit = sellPriceCandidate.getClosePrice() - minPrice;

				if (profit > maxProfit) {
					maxProfit = profit;
					sellDate = sellPriceCandidate.getDate();
					sellPrice = sellPriceCandidate.getClosePrice();
				}
			}
		}

		if (buyDate == null || sellDate == null || buyDate.isAfter(sellDate)) {
			return new ProfitResult(null, null, 0, 0, 0);
		}

		return new ProfitResult(buyDate, sellDate, buyPrice, sellPrice, maxProfit);
	}

	public double calculateTotalProfit(List<StockPrice> prices) {
		if (prices.isEmpty()) {
			return 0.0;
		}

		prices.sort(Comparator.comparing(StockPrice::getDate));

		double totalProfit = 0.0;
		for (int i = 1; i < prices.size(); i++) {
			double profit = prices.get(i).getClosePrice() - prices.get(i - 1).getClosePrice();
			if (profit > 0) {
				totalProfit += profit;
			}
		}
		return totalProfit;
	}
}
