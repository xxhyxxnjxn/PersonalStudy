package gmc.rd.report.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Report;

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


}
