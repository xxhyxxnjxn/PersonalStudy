package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;

public interface ReportRepository3 extends JpaRepository<Report3, Integer> {

	List<Report3> findByCurrency(String currency);
	
	@Query(value = "select * from reportco_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type='매도' and revenue is not null and accUnits='0.0' order by transactionDate desc);", nativeQuery = true)
	List<Report3> findByCurrency2(String currency);

	@Query(value = "select * from reportco_tbl where memId = :memId and orderId = :orderId", nativeQuery = true)
	Report3 findByOrderId(String orderId, String memId);
	
	
	@Query(value = "select * from reportco_tbl where memId = :memId and currency = :currency order by transactionDate desc limit 1", nativeQuery = true)
	Report3 findByOrderIdOne(String currency, String memId);
	
	@Transactional
	@Modifying
	@Query(value = "update reportco_tbl set site = :site, orderId = :orderId, transactionDate = :transactionDate, currency = :currency, type = :type, price = :price, units = :units, bidUnitsCal = :bidUnitsCal, askUnitsCal = :askUnitsCal, bidAvgPrice = :bidAvgPrice, askAvgPrice = :askAvgPrice, fee = :fee, totalPrice = :totalPrice, bidTotalPriceCal = :bidTotalPriceCal, askTotalPriceCal = :askTotalPriceCal, revenue = :revenue, accUnits = :accUnits, cal = :cal, totalRate = :totalRate, totalRateCal = :totalRateCal, expectIncome = :expectIncome, incomeCal = :incomeCal, income = :income, memId = :memId, askAccUnits = :askAccUnits, bidAccUnits = :bidAccUnits  where orderId = :orderId", nativeQuery = true)
	int updateReport(String orderId, String site, String transactionDate, String currency, String type, String price, String units, String bidUnitsCal, String askUnitsCal, String bidAvgPrice, String askAvgPrice, String fee, String totalPrice, String bidTotalPriceCal, String askTotalPriceCal, String revenue, String accUnits, String cal, String totalRate, String totalRateCal, String expectIncome, String incomeCal, String income, String memId,String askAccUnits,String bidAccUnits);

	@Query(value = "select * from reportco_tbl order by currency , transactionDate ASC", nativeQuery = true)
	List<Report3> findByabc();

	@Query(value = "select * from reportco_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report3> groupByCurrency(String memId);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and accUnits is null group by currency", nativeQuery = true)
	List<Report3> groupByCurrencyUpdate(String memId);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report3> groupByCurrencyBankStateMent(String memId);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report3> groupByCurrencyBankStateMentUpdate(String memId);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and currency = :currency order by transactionDate desc", nativeQuery = true)
	List<Report3> orderByTransactionDate(String memId, String currency);
	
	@Query(value = "select * FROM reportco_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate asc", nativeQuery = true)
	List<Report3> findByNull(String memId, String currency);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and currency = :currency and transactionDate < ( select min(transactionDate) FROM reportco_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate desc) order by transactionDate desc limit 1", nativeQuery = true)
	List<Report3> findByLatestRow(String memId, String currency);

	@Transactional
	@Modifying 
	@Query(value = "DELETE FROM reportco_tbl WHERE MEMID = :memId", nativeQuery = true)
	int deleteByMemId(String memId);
	
	
	
	@Query(value = "select * from reportco_tbl where memId = :memId and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report3> findByBankStateMent(String memId,String currency);
	
	@Query(value = "select * from reportco_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report3> findByBankStateMentUpdate(String memId,String currency);
	
	@Transactional
	@Modifying
	@Query(value = "update reportco_tbl set price = :price where orderId = :orderId", nativeQuery = true)
	int updateBankStateMentPrice(String orderId, String price);
	
	@Transactional
	@Modifying
	@Query(value = "update reportco_tbl set price = '0' where currency = :currency and memId = :memId and (type = '입금' or type = :출금)", nativeQuery = true)
	int updateBankStateMentPriceError(String currency,String memId);
	
	@Transactional
	@Modifying 
	@Query(value = "delete from reportco_tbl where orderId = :orderId", nativeQuery = true)
	int deleteByList(String orderId);
//	@Query(value = "SELECT count(*) as count FROM reportco_tbl where memId = :memId", nativeQuery = true)
//	List<String> findByCount(String memId);
}
