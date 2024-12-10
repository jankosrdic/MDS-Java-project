package com.mycompany.mds.model.database.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false, unique = true)
	private String tickerSymbol;

	private LocalDate dateFounded;

	private String industry;

	public Stock() {
	}

	public Stock(String companyName, String tickerSymbol, LocalDate dateFounded, String industry) {
		this.companyName = companyName;
		this.tickerSymbol = tickerSymbol;
		this.dateFounded = dateFounded;
		this.industry = industry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public LocalDate getDateFounded() {
		return dateFounded;
	}

	public void setDateFounded(LocalDate dateFounded) {
		this.dateFounded = dateFounded;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Override
	public String toString() {
		return "Stock{"
			+ "id=" + id
			+ ", companyName='" + companyName + '\''
			+ ", tickerSymbol='" + tickerSymbol + '\''
			+ ", dateFounded=" + dateFounded
			+ ", industry='" + industry + '\''
			+ '}';
	}
}
