package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;

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

}
