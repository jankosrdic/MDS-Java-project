package com.mycompany.mds.model.database.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class StockPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tickerSymbol; // company symbol
	private LocalDate date; 
	private double openPrice;
	private double highPrice; 
	private double lowPrice;
	private double closePrice;
	private double adjClosePrice;
	private long volume;

	//need this for JPA
	public StockPrice() {
	}

	public StockPrice(String tickerSymbol, LocalDate date, double openPrice, double highPrice, double lowPrice, double closePrice, double adjClosePrice, long volume) {
		this.tickerSymbol = tickerSymbol;
		this.date = date;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.adjClosePrice = adjClosePrice;
		this.volume = volume;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	public double getAdjClosePrice() {
		return adjClosePrice;
	}

	public void setAdjClosePrice(double adjClosePrice) {
		this.adjClosePrice = adjClosePrice;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "StockPrice{"
			+ "id=" + id
			+ ", tickerSymbol='" + tickerSymbol + '\''
			+ ", date=" + date
			+ ", openPrice=" + openPrice
			+ ", highPrice=" + highPrice
			+ ", lowPrice=" + lowPrice
			+ ", closePrice=" + closePrice
			+ ", adjClosePrice=" + adjClosePrice
			+ ", volume=" + volume
			+ '}';
	}
}
