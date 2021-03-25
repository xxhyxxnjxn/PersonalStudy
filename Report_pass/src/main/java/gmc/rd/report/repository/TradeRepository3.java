package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;

public interface TradeRepository3  extends JpaRepository<Report3, Integer>{
	
	@Query(value = "select * from reportco_tbl where memId = :memId group by site", nativeQuery = true)
	List<Report3> findGroupbySite(String memId);

	@Query(value = "select * from reportco_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report3> findGroupbyCoin(String memId);

	@Query(value = "select * from reportco_tbl where site = :site", nativeQuery = true)
	List<Report3> findBySite(String site);

	@Query(value = "select * from reportco_tbl where memId = :memId order by transactionDate asc", nativeQuery = true)
	List<Report3> findByMemIdAsc(String memId);
	@Query(value = "select * from reportco_tbl where memId = :memId order by transactionDate Desc", nativeQuery = true)
	List<Report3> findByMemIdDesc(String memId);
	@Query(value = "select * from reportco_tbl where memId = :memId and (type = '출금' or type =  '입금')", nativeQuery = true)
	List<Report3> findByBankStatement(String memId);

	Page<Report3> findByUser(User user, Pageable page);

	Page<Report3> findByUserAndCurrency(User user, String currency, Pageable page);

	Page<Report3> findByUserAndTransactionDateBetween(User user, String start2, String end2, Pageable page);

	Page<Report3> findByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2,
			Pageable page);

	Report3 findByOrderId(String orderId);

	Page<Report3> findByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2, Pageable page);

	Page<Report3> findByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2,
			Pageable page);

	Page<Report3> findByUserAndCurrencyAndType(User user, String currency, String type, Pageable page);

	Page<Report3> findByUserAndType(User user, String type, Pageable page);
	
	long countByUser(User user);

	long countByUserAndCurrency(User user, String currency);

	long countByUserAndTransactionDateBetween(User user, String start2, String end2);

	long countByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2);


	long countByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2);

	long countByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2);

	long countByUserAndCurrencyAndType(User user, String currency, String type);

	long countByUserAndType(User user, String type);
	
	@Query(value = "SELECT MAX(transactionDate) as max FROM reportco_tbl where memId = :memId", nativeQuery = true)
	String findByMaxTransaction(String memId);
	
	@Query(value = "SELECT min(transactionDate) as min FROM reportco_tbl where memId = :memId", nativeQuery = true)
	String findByMinTransaction(String memId);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and price = '0.0' and (type = '출금' or type =  '입금') order by transactionDate desc", nativeQuery = true)
	List<Report3> findByBankStatement0(String memId);

}
