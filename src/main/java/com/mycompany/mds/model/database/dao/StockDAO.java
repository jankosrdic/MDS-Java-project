package com.mycompany.mds.model.database.dao;

import com.mycompany.mds.model.database.entity.Stock;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import javax.persistence.NoResultException;

public class StockDAO {

	@Inject
	private EntityManager em;

	@Transactional
	public void save(Stock stock) {
		em.persist(stock);
	}

	public Stock findById(Long id) {
		return em.find(Stock.class, id);
	}

	public List<Stock> findAll() {
		return em.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
	}

	@Transactional
	public void update(Stock stock) {
		em.merge(stock);
	}

	@Transactional
	public void delete(Long id) {
		Stock stock = em.find(Stock.class, id);
		if (stock != null) {
			em.remove(stock);
		}
	}

	@Transactional
	public void deleteAll() {
		em.createQuery("DELETE FROM Stock").executeUpdate();
	}

	public String findTickerByCompanyName(String companyName) {
		try {
			String query = "SELECT s.tickerSymbol FROM Stock s WHERE LOWER(s.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))";
			return em.createQuery(query, String.class)
				.setParameter("companyName", companyName)
				.setMaxResults(1)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean existsByTickerSymbol(String tickerSymbol) {
		String query = "SELECT COUNT(s) FROM Stock s WHERE s.tickerSymbol = :tickerSymbol";
		Long count = em.createQuery(query, Long.class)
			.setParameter("tickerSymbol", tickerSymbol)
			.getSingleResult();
		return count > 0;
	}
}
