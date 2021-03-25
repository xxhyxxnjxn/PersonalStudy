package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.RevenueState;

public interface RevenueStateRepository extends JpaRepository<RevenueState, Integer> {

	
	RevenueState findByMemIdAndSite(String memId, String site);
	@Transactional
	@Modifying 
	@Query(value = "UPDATE revenuestate SET state = :b WHERE memId = :memId and site = :site", nativeQuery = true)
	int updatestate(String memId, String site, boolean b);
	
	@Transactional
	@Modifying 
	@Query(value = "delete from revenuestate where memId = :memId and site = :temSite", nativeQuery = true)
	int deleteByMemId(String temSite, String memId);
	
	@Transactional
	@Modifying 
	@Query(value = "update revenuestate set num = :num where memId = :memId and site = :site", nativeQuery = true)
	void updateAsNum(String num, String memId, String site);
	
	@Transactional
	@Modifying 
	@Query(value = "update revenuestate set totalNum = :totalNum where memId = :memId and site = :site", nativeQuery = true)
	void updateAsTotalNum(String totalNum, String memId, String site);

}
