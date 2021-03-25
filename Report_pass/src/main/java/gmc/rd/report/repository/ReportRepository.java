package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;
import gmc.rd.report.entity.Vm;

public interface ReportRepository extends JpaRepository<Report, Integer> {

	List<Report> findByCurrency(String currency);
	
	@Query(value = "select * from reportbi_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type='매도' and revenue is not null and accUnits='0.0' order by transactionDate desc);", nativeQuery = true)
	List<Report> findByCurrency2(String currency);

	@Query(value = "select * from reportbi_tbl where memId = :memId and orderId = :orderId ", nativeQuery = true)
	Report findByOrderId(String orderId, String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and currency = :currency order by transactionDate desc limit 1", nativeQuery = true)
	Report findByOrderIdOne(String currency, String memId);

	@Transactional
	@Modifying
	@Query(value = "update reportbi_tbl set site = :site, orderId = :orderId, transactionDate = :transactionDate, currency = :currency, type = :type, price = :price, units = :units, bidUnitsCal = :bidUnitsCal, askUnitsCal = :askUnitsCal, bidAvgPrice = :bidAvgPrice, askAvgPrice = :askAvgPrice, fee = :fee, totalPrice = :totalPrice, bidTotalPriceCal = :bidTotalPriceCal, askTotalPriceCal = :askTotalPriceCal, revenue = :revenue, accUnits = :accUnits, cal = :cal, totalRate = :totalRate, totalRateCal = :totalRateCal, expectIncome = :expectIncome, incomeCal = :incomeCal, income = :income, memId = :memId, askAccUnits = :askAccUnits, bidAccUnits = :bidAccUnits where orderId = :orderId", nativeQuery = true)
	int updateReport(String orderId, String site, String transactionDate, String currency, String type, String price, String units, String bidUnitsCal, String askUnitsCal, String bidAvgPrice, String askAvgPrice, String fee, String totalPrice, String bidTotalPriceCal, String askTotalPriceCal, String revenue, String accUnits, String cal, String totalRate, String totalRateCal, String expectIncome, String incomeCal, String income, String memId,String askAccUnits,String bidAccUnits);
	
	@Query(value = "select * from reportbi_tbl order by currency , transactionDate ASC", nativeQuery = true)
	List<Report> findByabc();

	@Query(value = "select * from reportbi_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report> groupByCurrency(String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and accUnits is null group by currency", nativeQuery = true)
	List<Report> groupByCurrencyUpdate(String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report> groupByCurrencyBankStateMent(String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') group by currency", nativeQuery = true)
	List<Report> groupByCurrencyBankStateMentUpdate(String memId);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and currency = :currency order by transactionDate desc", nativeQuery = true)
	List<Report> orderByTransactionDate(String memId, String currency);
	
	@Query(value = "select * FROM reportbi_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate asc", nativeQuery = true)
	List<Report> findByNull(String memId, String currency);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and currency = :currency and transactionDate < ( select min(transactionDate) FROM reportbi_tbl where memId = :memId and currency = :currency and accUnits is null order by transactionDate desc) order by transactionDate desc limit 1", nativeQuery = true)
	List<Report> findByLatestRow(String memId, String currency);

	@Transactional
	@Modifying 
	@Query(value = "DELETE FROM reportbi_tbl WHERE MEMID = :memId", nativeQuery = true)
	int deleteByMemId(String memId);

	@Query(value = "select * from reportbi_tbl where memId = :memId and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report> findByBankStateMent(String memId,String currency);
	
	@Query(value = "select * from reportbi_tbl where memId = :memId and price = '0.0' and (type = '입금' or type = '출금') and currency = :currency", nativeQuery = true)
	List<Report> findByBankStateMentUpdate(String memId,String currency);
	
	@Transactional
	@Modifying
	@Query(value = "update reportbi_tbl set price = :price where orderId = :orderId", nativeQuery = true)
	int updateBankStateMentPrice(String orderId, String price);
	
	@Transactional
	@Modifying 
	@Query(value = "delete from reportbi_tbl where orderId = :orderId", nativeQuery = true)
	int deleteByList(String orderId);
	
//	@Query(value = "SELECT count(*) as count FROM reportbi_tbl where memId = :memId and currency = :currency and type = :type and (transactionDate between :start and :end)", nativeQuery = true)
//	List<String> findByCount(String memId, String currency, String start,String end, String type);
	
//	@Query(value = "select @rownum := @rownum +1 as rownum ,reportbi_tbl.transactionDate as date from reportbi_tbl,(select @rownum :=0 ) tmp where reportbi_tbl.memId = :memId order by transactionDate asc", nativeQuery = true)
//	List<String> bithumbRowNum(String memId);
}
