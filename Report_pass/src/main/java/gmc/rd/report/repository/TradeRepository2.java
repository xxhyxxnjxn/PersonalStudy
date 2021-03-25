package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.dto.CountDto;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;

public interface TradeRepository2 extends JpaRepository<Report2, Integer> {

	@Query(value = "select * from reportup_tbl where memId = :memId group by site", nativeQuery = true)
	List<Report2> findGroupbySite(String memId);

	@Query(value = "select * from reportup_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report2> findGroupbyCoin(String memId);

	@Query(value = "select * from reportup_tbl where site = :site", nativeQuery = true)
	List<Report2> findBySite(String site);

	@Query(value = "select * from reportup_tbl where memId = :memId order by transactionDate asc", nativeQuery = true)
	List<Report2> findByMemIdAsc(String memId);

	@Query(value = "select * from reportup_tbl where memId = :memId and (type = '출금' or type =  '입금') order by transactionDate desc", nativeQuery = true)
	List<Report2> findByBankStatement(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and (type = '출금' or type =  '입금') order by transactionDate asc", nativeQuery = true)
	List<Report2> findByBankStatement2(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and type = :type order by transactionDate desc", nativeQuery = true)
	List<Report2> findByBankStatementandType(String memId,String type);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and type = :type order by transactionDate asc", nativeQuery = true)
	List<Report2> findByBankStatementandType2(String memId,String type);
	
	@Query(value = "select * from reportup_tbl where price = :priceSelect and memId = :memId and (type = '출금' or type =  '입금') order by transactionDate desc", nativeQuery = true)
	List<Report2> findByBankStatement(String memId,String priceSelect);
	
	@Query(value = "select * from reportup_tbl where price = :priceSelect and emId = :memId and (type = '출금' or type =  '입금') order by transactionDate asc", nativeQuery = true)
	List<Report2> findByBankStatement2(String memId,String priceSelect);
	
	@Query(value = "select * from reportup_tbl where price = :priceSelect and memId = :memId and type = :type order by transactionDate desc", nativeQuery = true)
	List<Report2> findByBankStatementandType(String memId,String type,String priceSelect);
	
	@Query(value = "select * from reportup_tbl where price = :priceSelect and memId = :memId and type = :type order by transactionDate asc", nativeQuery = true)
	List<Report2> findByBankStatementandType2(String memId,String type,String priceSelect);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and price = '0.0' and (type = '출금' or type =  '입금') order by transactionDate desc", nativeQuery = true)
	List<Report2> findByBankStatement0(String memId);

	
	Page<Report2> findByUserAndType(User user, String type, Pageable pageable);
	
	Page<Report2> findByUser(User user, Pageable pageable);
	
	Page<Report2> findByUserAndTransactionDateBetween(User user,String start,String end, Pageable pageable);

	Page<Report2> findAll(Pageable pageable);

	Page<Report2> findByUserAndCurrency(User user, String currency, Pageable page);

	Page<Report2> findByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2,	Pageable page);

	Report2 findByOrderId(String orderId);

	Page<Report2> findByUserAndCurrencyAndType(User user, String currency, String type, Pageable page);

	Page<Report2> findByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2,
			Pageable page);

	Page<Report2> findByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2, Pageable page);
	
	long countByUser(User user);

	long countByUserAndCurrency(User user, String currency);

	long countByUserAndTransactionDateBetween(User user, String start2, String end2);

	long countByUserAndCurrencyAndTransactionDateBetween(User user, String currency, String start2, String end2);


	long countByUserAndCurrencyAndTypeAndTransactionDateBetween(User user, String currency, String type,
			String start2, String end2);

	long countByUserAndTypeAndTransactionDateBetween(User user, String type, String start2, String end2);

	long countByUserAndCurrencyAndType(User user, String currency, String type);

	long countByUserAndType(User user, String type);

	@Query(value = "SELECT MAX(transactionDate) as max FROM reportup_tbl where memId = :memId", nativeQuery = true)
	String findByMaxTransaction(String memId);
	@Query(value = "SELECT min(transactionDate) as min FROM reportup_tbl where memId = :memId", nativeQuery = true)
	String findByMinTransaction(String memId);
	
	@Query(value = "SELECT type FROM reportup_tbl where memId = :memId", nativeQuery = true)
	List<String> findByType(String memId);
	
//	Page<Report2> findByMemId(String memId, Pageable pageable);

}
