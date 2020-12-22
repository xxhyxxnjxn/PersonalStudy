package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report2;

public interface ReportRepository2 extends JpaRepository<Report2, Integer> {

	List<Report2> findByCurrency(String currency);
	
	@Query(value = "select * from reportup_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type='매도' and revenue is not null and accUnits='0.0' order by transactionDate desc);", nativeQuery = true)
	List<Report2> findByCurrency2(String currency);

	@Query(value = "select * from reportup_tbl where orderId = :orderId ", nativeQuery = true)
	Report2 findByOrderId(String orderId);

	@Query(value = "select * from reportup_tbl order by currency , transactionDate ASC", nativeQuery = true)
	List<Report2> findByabc();

}
