package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;

public interface TradeRepository2  extends JpaRepository<Report2, Integer>{
	
	@Query(value = "select * from reportup_tbl where memId = :memId group by site", nativeQuery = true)
	List<Report2> findGroupbySite(String memId);

	@Query(value = "select * from reportup_tbl where memId = :memId group by currency", nativeQuery = true)
	List<Report2> findGroupbyCoin(String memId);
	
	@Query(value = "select * from reportup_tbl where site = :site", nativeQuery = true)
	List<Report2> findBySite(String site);

	@Query(value = "select * from reportup_tbl where memId = :memId order by transactionDate asc", nativeQuery = true)
	List<Report2> findByMemIdAsc(String memId);

	@Query(value = "select * from reportup_tbl where memId = :memId order by transactionDate Desc", nativeQuery = true)
	List<Report2> findByMemIdDesc(String memId);
	

}
