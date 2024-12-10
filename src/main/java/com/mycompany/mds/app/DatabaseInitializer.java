package com.mycompany.mds.app;

import com.mycompany.mds.model.database.dao.StockDAO;
import com.mycompany.mds.model.database.dao.StockPriceDAO;
import com.mycompany.mds.model.database.entity.Stock;
import com.mycompany.mds.model.database.util.CSVDataLoader;
import java.io.File;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@Singleton
@Startup
public class DatabaseInitializer {

	@Inject
	private StockDAO stockDAO;

	@Inject
	private StockPriceDAO stockPriceDAO;

	@Inject
	private CSVDataLoader csvDataLoader;

	@PostConstruct
	public void init() {

		//System.out.println("Deleting existing data in database...");
		//clearDatabase();
		System.out.println("Initializing data from CSV files...");

		try {
			// Path to CSV files for data import, change if needed
			String csvDirectoryPath = "/home/anders/Downloads/MDS";

			File csvDirectory = new File(csvDirectoryPath);
			if (!csvDirectory.exists() || !csvDirectory.isDirectory()) {
				System.out.println("CSV directory does not exist or is not a directory: " + csvDirectoryPath);
			} else {
				csvDataLoader.loadCSVFiles(csvDirectoryPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while importing from CSV file.", e);
		}

		System.out.println("Initializing Stock company`s data");

		initializeStocks();
		printDatabaseContents();
	}

	private void clearDatabase() {
		stockPriceDAO.deleteAll();
		stockDAO.deleteAll();
		System.out.println("All tables cleared");
	}

	private void initializeStocks() {
		List<Stock> stocks = List.of(
			new Stock("Apple Inc.", "AAPL", LocalDate.of(1976, 4, 1), "Technology"),
			new Stock("Amazon.com, Inc.", "AMZN", LocalDate.of(1994, 7, 5), "E-commerce"),
			new Stock("Facebook, Inc.", "META", LocalDate.of(2004, 2, 4), "Social Media"),
			new Stock("Google LLC", "GOOG", LocalDate.of(1998, 9, 4), "Search Engine"),
			new Stock("Netflix, Inc.", "NFLX", LocalDate.of(1997, 8, 29), "Streaming")
		);

		for (Stock stock : stocks) {
			if (!isStockExists(stock.getTickerSymbol())) {
				stockDAO.save(stock);
			} else {
				System.out.println("Already exists in database: " + stock.getTickerSymbol());
			}
		}
	}

	private boolean isStockExists(String tickerSymbol) {
		return stockDAO.findAll().stream().anyMatch(stock -> stock.getTickerSymbol().equalsIgnoreCase(tickerSymbol));
	}

	private void printDatabaseContents() {
		System.out.println("\nDisplaying all companies in database:");
		stockDAO.findAll().forEach(System.out::println);
	}
}
