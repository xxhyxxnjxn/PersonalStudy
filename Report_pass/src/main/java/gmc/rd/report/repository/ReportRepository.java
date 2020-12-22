package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Vm;

public interface ReportRepository extends JpaRepository<Report, Integer> {

	List<Report> findByCurrency(String currency);
	
	@Query(value = "select * from reportbi_tbl where currency = :currency and transactionDate > all (select transactionDate from report_tbl where currency = :currency and type='매도' and revenue is not null and accUnits='0.0' order by transactionDate desc);", nativeQuery = true)
	List<Report> findByCurrency2(String currency);

	@Query(value = "select * from reportbi_tbl where orderId = :orderId ", nativeQuery = true)
	Report findByOrderId(String orderId);

	@Query(value = "select * from reportbi_tbl order by currency , transactionDate ASC", nativeQuery = true)
	List<Report> findByabc();


}
