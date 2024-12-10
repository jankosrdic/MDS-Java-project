package com.mycompany.mds.controller;

import com.mycompany.mds.model.database.dao.StockPriceDAO;
import com.mycompany.mds.model.service.ProfitAnalyzer;
import com.mycompany.mds.model.service.ProfitAnalyzer.ProfitResult;
import com.mycompany.mds.model.database.entity.StockPrice;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Path("/profit")
public class ProfitController {

	@Inject
	private StockPriceDAO stockPriceDAO;

	@Inject
	private ProfitAnalyzer profitAnalyzer;

	@GET
	@Path("/{tickerSymbol}")
	@Produces("application/json")
	public Response calculateProfit(
		@PathParam("tickerSymbol") String tickerSymbol,
		@QueryParam("startDate") String startDateStr,
		@QueryParam("endDate") String endDateStr
	) {
		try {
			LocalDate startDate = LocalDate.parse(startDateStr);
			LocalDate endDate = LocalDate.parse(endDateStr);

			if (startDate.isAfter(endDate)) {
				return Response.status(Response.Status.BAD_REQUEST)
					.entity(Map.of("error", "Start date must be before or equal to end date."))
					.build();
			}

			long mainPeriodLength = ChronoUnit.DAYS.between(startDate, endDate) + 1;

			LocalDate previousStartDate = startDate.minusDays(mainPeriodLength);
			LocalDate previousEndDate = startDate.minusDays(1);

			LocalDate nextStartDate = endDate.plusDays(1);
			LocalDate nextEndDate = endDate.plusDays(mainPeriodLength);

			List<StockPrice> previousPrices = stockPriceDAO.findPricesInRange(tickerSymbol, previousStartDate, previousEndDate);
			List<StockPrice> currentPrices = stockPriceDAO.findPricesInRange(tickerSymbol, startDate, endDate);
			List<StockPrice> nextPrices = stockPriceDAO.findPricesInRange(tickerSymbol, nextStartDate, nextEndDate);

			if (currentPrices.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND)
					.entity(Map.of("error", "No stock data found for the main period."))
					.build();
			}

			ProfitResult previousProfit = previousPrices.isEmpty()
				? new ProfitResult(null, null, 0.0, 0.0, 0.0)
				: profitAnalyzer.calculateMaxProfit(previousPrices);

			ProfitResult currentProfit = profitAnalyzer.calculateMaxProfit(currentPrices);

			ProfitResult nextProfit;
			if (nextPrices.isEmpty()) {
				nextProfit = new ProfitResult(null, null, 0.0, 0.0, 0.0);
			} else {
				nextPrices.sort(Comparator.comparing(StockPrice::getDate));
				nextProfit = profitAnalyzer.calculateMaxProfit(nextPrices);
			}

			double totalProfit = profitAnalyzer.calculateTotalProfit(currentPrices);

			List<String> allTickerSymbols = stockPriceDAO.findAllTickerSymbolsWithPricesInRange(startDate, endDate);
			List<String> higherProfitSymbols = new ArrayList<>();

			for (String symbol : allTickerSymbols) {
				if (!symbol.equals(tickerSymbol)) {
					List<StockPrice> prices = stockPriceDAO.findPricesInRange(symbol, startDate, endDate);
					double otherTotalProfit = profitAnalyzer.calculateTotalProfit(prices);
					if (otherTotalProfit > totalProfit) {
						higherProfitSymbols.add(symbol);
					}
				}
			}

			return Response.ok(Map.of(
				"previousPeriodProfit", previousProfit,
				"mainPeriodProfit", currentProfit,
				"nextPeriodProfit", nextProfit,
				"totalProfit", totalProfit,
				"higherProfitSymbols", higherProfitSymbols // Nova funkcionalnost
			)).build();

		} catch (DateTimeParseException e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(Map.of("error", "Invalid date format. Use 'YYYY-MM-DD'."))
				.build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(Map.of("error", "An unexpected error occurred: " + e.getMessage()))
				.build();
		}
	}
}
