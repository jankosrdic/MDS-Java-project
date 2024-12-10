package com.mycompany.mds.model.database.util;

import com.mycompany.mds.model.database.dao.StockDAO;
import com.mycompany.mds.model.database.dao.StockPriceDAO;
import com.mycompany.mds.model.database.entity.StockPrice;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class CSVDataLoader {

	@Inject
	private StockDAO stockDAO;

	@Inject
	private StockPriceDAO stockPriceDAO;

	public void loadCSVFiles(String directoryPath) {
		try {
			Files.list(Paths.get(directoryPath))
				.filter(Files::isRegularFile)
				.filter(file -> file.toString().endsWith(".csv"))
				.forEach(this::processCSVFile);
		} catch (IOException e) {
			throw new RuntimeException("Error with files in directory: " + directoryPath, e);
		}
	}

	private void processCSVFile(Path csvFilePath) {
		String fileName = csvFilePath.getFileName().toString();
		String companyName = fileName.substring(0, fileName.lastIndexOf('.'));

		// Pronaći ticker simbol za kompaniju
		String tickerSymbol = stockDAO.findTickerByCompanyName(companyName);
		if (tickerSymbol == null) {
			System.out.printf("Ticker symbol not found for company: %s%n", companyName);
			return;
		}

		System.out.printf("Working on file: %s, ticker symbol: %s%n", fileName, tickerSymbol);

		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath.toString()))) {
			String line;
			int lineNumber = 0;

			while ((line = br.readLine()) != null) {
				lineNumber++;

				if (lineNumber == 1) {
					continue;
				}

				try {
					parseAndSaveStockPrice(line, tickerSymbol);
				} catch (Exception e) {
					System.out.printf("Error reading line %d in file %s: %s%n", lineNumber, fileName, e.getMessage());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error reading file: " + csvFilePath, e);
		}
	}

	private void parseAndSaveStockPrice(String line, String tickerSymbol) {
		String[] values = line.split(",");
		if (values.length < 7) {
			System.out.println("Wrong format in column: " + line);
			return;
		}

		try {
			LocalDate date = LocalDate.parse(values[0].trim());
			double openPrice = Double.parseDouble(values[1].trim());
			double highPrice = Double.parseDouble(values[2].trim());
			double lowPrice = Double.parseDouble(values[3].trim());
			double closePrice = Double.parseDouble(values[4].trim());
			double adjClosePrice = Double.parseDouble(values[5].trim());
			long volume = Long.parseLong(values[6].trim());

			if (!stockPriceDAO.existsByTickerSymbolAndDate(tickerSymbol, date)) {
				StockPrice stockPrice = new StockPrice(
					tickerSymbol, date, openPrice, highPrice, lowPrice, closePrice, adjClosePrice, volume
				);
				stockPriceDAO.save(stockPrice);
			}
		} catch (Exception e) {
			throw new RuntimeException("Parsing error for line: " + line, e);
		}
	}
}
