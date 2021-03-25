package gmc.rd.report.repository;

import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;

public interface TradeRepository  extends JpaRepository<Report, Integer>{
	
	@Query(value = "select * from reportbi_tbl where memId = :memId group by site", nativeQuery = true)
	List<Report> findGroupbySite(String memId);

	@Query(value = "select * from reportbi_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report> findGroupbyCoin(String memId);

	@Query(value = "select * from reportbi_tbl where site = :site", nativeQuery = true)
	List<Report> findBySite(String site);

	@Query(value = "select * from reportbi_tbl where memId = :memId order by transactionDate asc", nativeQuery = true)
	List<Report> findByMemIdAsc(String memId);

	@Query(value = "select * from reportbi_tbl where memId = :memId order by transactionDate Desc", nativeQuery = true)
	List<Report> findByMemIdDesc(String memId);
	

	@Query(value = "select * from reportbi_tbl where memId = :memId and (type = '출금' or type =  '입금')", nativeQuery = true)
	List<Report> findByBankStatement(String memId);

	Page<Report> findByUser(User user, Pageable page);

	Page<Report> findByUserAndCurrency(User user, String currency, Pageable page);

	Page<Report> findByUserAndTransactionDateBetween(User user, String start2, String end2,
			Pageable page);

	Page<Report> findByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2,
			Pageable page);

	Report findByOrderId(String orderId);

	Page<Report> findByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2, Pageable page);

	Page<Report> findByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2,
			Pageable page);

	Page<Report> findByUserAndCurrencyAndType(User user, String currency, String type, Pageable page);

	Page<Report> findByUserAndType(User user, String type, Pageable page);
	
	long countByUser(User user);

	long countByUserAndCurrency(User user, String currency);

	long countByUserAndTransactionDateBetween(User user, String start2, String end2);

	long countByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2);


	long countByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2);

	long countByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2);

	long countByUserAndCurrencyAndType(User user, String currency, String type);

	long countByUserAndType(User user, String type);
	
	@Query(value = "SELECT MAX(transactionDate) as max FROM reportbi_tbl where memId = :memId", nativeQuery = true)
	String findByMaxTransaction(String memId);
	
	@Query(value = "SELECT min(transactionDate) as min FROM reportbi_tbl where memId = :memId", nativeQuery = true)
	String findByMinTransaction(String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and price = '0.0' and (type = '출금' or type =  '입금') order by transactionDate desc", nativeQuery = true)
	List<Report> findByBankStatement0(String memId);

	

}
