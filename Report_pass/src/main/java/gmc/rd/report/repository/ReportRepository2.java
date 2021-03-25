package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;

public interface ReportRepository2 extends JpaRepository<Report2, Integer> {

	List<Report2> findByCurrency(String currency);
	
	@Query(value = "select * from reportup_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type= 매도  and revenue is not null and accUnits= 0.0  order by transactionDate desc);", nativeQuery = true)
	List<Report2> findByCurrency2(String currency);

	@Query(value = "select * from reportup_tbl where memId = :memId and orderId = :orderId ", nativeQuery = true)
	Report2 findByOrderId(String orderId,String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and currency = :currency order by transactionDate desc limit 1", nativeQuery = true)
	Report2 findByOrderIdOne(String currency, String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and currency = :currency and type = '입금' order by transactionDate desc limit 1", nativeQuery = true)
	Report2 findByOrderIdOneDeposite(String currency, String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and currency = :currency and type = '출금' order by transactionDate desc limit 1", nativeQuery = true)
	Report2 findByOrderIdOneWithdraw(String currency, String memId);
	
	@Transactional
	@Modifying
	@Query(value = "update reportup_tbl set site = :site, orderId = :orderId, transactionDate = :transactionDate, currency = :currency, type = :type, price = :price, units = :units, bidUnitsCal = :bidUnitsCal, askUnitsCal = :askUnitsCal, bidAvgPrice = :bidAvgPrice, askAvgPrice = :askAvgPrice, fee = :fee, totalPrice = :totalPrice, bidTotalPriceCal = :bidTotalPriceCal, askTotalPriceCal = :askTotalPriceCal, revenue = :revenue, accUnits = :accUnits, cal = :cal, totalRate = :totalRate, totalRateCal = :totalRateCal, expectIncome = :expectIncome, incomeCal = :incomeCal, income = :income, memId = :memId, askAccUnits = :askAccUnits, bidAccUnits = :bidAccUnits where orderId = :orderId", nativeQuery = true)
	int updateReport(String orderId, String site, String transactionDate, String currency, String type, String price, String units, String bidUnitsCal, String askUnitsCal, String bidAvgPrice, String askAvgPrice, String fee, String totalPrice, String bidTotalPriceCal, String askTotalPriceCal, String revenue, String accUnits, String cal, String totalRate, String totalRateCal, String expectIncome, String incomeCal, String income, String memId,String askAccUnits,String bidAccUnits);

	@Query(value = "select * from reportup_tbl where memId = :memId order by currency , transactionDate ASC", nativeQuery = true)
	List<Report2> findByabc(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report2> groupByCurrency(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and accUnits is null group by currency", nativeQuery = true)
	List<Report2> groupByCurrencyUpdate(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report2> groupByCurrencyBankStateMent(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report2> groupByCurrencyBankStateMentUpdate(String memId);

	@Query(value = "select * from reportup_tbl where memId = :memId and currency = :currency order by transactionDate asc", nativeQuery = true)
	List<Report2> orderByTransactionDate(String memId, String currency);
	
	@Query(value = "select * FROM reportup_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate asc", nativeQuery = true)
	List<Report2> findByNull(String memId, String currency);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and currency = :currency and transactionDate < ( select min(transactionDate) FROM reportup_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate desc) order by transactionDate desc limit 1", nativeQuery = true)
	List<Report2> findByLatestRow(String memId, String currency);

	@Transactional
	@Modifying 
	@Query(value = "delete from reportup_tbl where memId = :memId", nativeQuery = true)
	int deleteByMemId(String memId);

	@Query(value = "select * FROM reportup_tbl WHERE MEMID = :memId", nativeQuery = true)
	List<Report2> findByMemId(String memId);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and units = :units and currency = :currency and type = :type and price = '0.0' and  (transactionDate >= :today  AND transactionDate < :todayPlus)", nativeQuery = true)
	List<Report2> findByOrderIdExcel(String memId, String units, String currency, String type, String today, String todayPlus);
	
	@Transactional
	@Modifying
	@Query(value = "update reportup_tbl set price = :price WHERE  orderId = :orderId and units = :units and currency = :currency and type= :type and price = '0.0' and (transactionDate >= :today  AND transactionDate < :todayPlus);", nativeQuery = true)
	int updateByPrice(String orderId, String price, String units, String currency, String type, String today, String todayPlus);
	
	
	@Query(value = "select * from reportup_tbl where memId = :memId and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report2> findByBankStateMent(String memId,String currency);
	
	@Query(value = "select * from reportup_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report2> findByBankStateMentUpdate(String memId,String currency);
	
	@Transactional
	@Modifying
	@Query(value = "update reportup_tbl set price = :price where orderId = :orderId", nativeQuery = true)
	int updateBankStateMentPrice(String orderId, String price);
	
	@Transactional
	@Modifying
	@Query(value = "update reportup_tbl set price = '0' where currency = :currency and memId = :memId and (type = '입금' or type = :출금)", nativeQuery = true)
	int updateBankStateMentPriceError(String currency,String memId);
	
	
	@Transactional
	@Modifying 
	@Query(value = "delete from reportup_tbl where orderId = :orderId", nativeQuery = true)
	int deleteByList(String orderId);

//	@Query(value = "SELECT count(*) as count FROM reportup_tbl where memId = :memId", nativeQuery = true)
//	List<String> findByCount(String memId);
	
}
