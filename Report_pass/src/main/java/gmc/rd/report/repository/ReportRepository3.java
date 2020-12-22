package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report3;

public interface ReportRepository3 extends JpaRepository<Report3, Integer> {

	List<Report3> findByCurrency(String currency);
	
	@Query(value = "select * from reportco_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type='매도' and revenue is not null and accUnits='0.0' order by transactionDate desc);", nativeQuery = true)
	List<Report3> findByCurrency2(String currency);

	@Query(value = "select * from reportco_tbl where orderId = :orderId ", nativeQuery = true)
	Report3 findByOrderId(String orderId);

	@Query(value = "select * from reportco_tbl order by currency , transactionDate ASC", nativeQuery = true)
	List<Report3> findByabc();

}
