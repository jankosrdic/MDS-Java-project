package com.mycompany.mds.model.database.dao;

import com.mycompany.mds.model.database.entity.StockPrice;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import javax.persistence.TypedQuery;

public class StockPriceDAO {

	@Inject
	private EntityManager em;

	@Transactional
	public void save(StockPrice stockPrice) {
		em.persist(stockPrice);
	}

	public List<StockPrice> findByTickerSymbol(String tickerSymbol) {
		return em.createQuery("FROM StockPrice sp WHERE sp.tickerSymbol = :tickerSymbol", StockPrice.class)
			.setParameter("tickerSymbol", tickerSymbol)
			.getResultList();
	}

	@Transactional
	public void deleteAll() {
		em.createQuery("DELETE FROM StockPrice").executeUpdate();
	}

	public boolean existsByTickerSymbolAndDate(String tickerSymbol, LocalDate date) {
		String query = "SELECT COUNT(sp) FROM StockPrice sp WHERE sp.tickerSymbol = :tickerSymbol AND sp.date = :date";
		Long count = em.createQuery(query, Long.class)
			.setParameter("tickerSymbol", tickerSymbol)
			.setParameter("date", date)
			.getSingleResult();
		return count > 0;
	}

	public List<StockPrice> findByTickerSymbolAndDateRange(String tickerSymbol, LocalDate startDate, LocalDate endDate) {
		return em.createQuery(
			"SELECT sp FROM StockPrice sp WHERE sp.tickerSymbol = :tickerSymbol AND sp.date BETWEEN :startDate AND :endDate",
			StockPrice.class)
			.setParameter("tickerSymbol", tickerSymbol)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate)
			.getResultList();
	}

	public List<StockPrice> findPricesInRange(String tickerSymbol, LocalDate startDate, LocalDate endDate) {
		String query = "SELECT sp FROM StockPrice sp WHERE sp.tickerSymbol = :tickerSymbol AND sp.date BETWEEN :startDate AND :endDate ORDER BY sp.date ASC";
		TypedQuery<StockPrice> typedQuery = em.createQuery(query, StockPrice.class);
		typedQuery.setParameter("tickerSymbol", tickerSymbol);
		typedQuery.setParameter("startDate", startDate);
		typedQuery.setParameter("endDate", endDate);
		return typedQuery.getResultList();
	}

	public List<String> findAllTickerSymbolsWithPricesInRange(LocalDate startDate, LocalDate endDate) {
		return em.createQuery(
			"SELECT DISTINCT sp.tickerSymbol FROM StockPrice sp WHERE sp.date BETWEEN :startDate AND :endDate",
			String.class
		)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate)
			.getResultList();
	}
}
