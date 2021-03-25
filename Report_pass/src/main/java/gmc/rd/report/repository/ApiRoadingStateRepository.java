package gmc.rd.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.ApiRoadingState;

public interface ApiRoadingStateRepository  extends JpaRepository<ApiRoadingState, Integer>{
	
	@Query(value = "select * from apiroadingstate where site = :site and memId= :memId", nativeQuery = true)
	ApiRoadingState findByMemIdAndSite(String memId, String site);

	@Transactional
	@Modifying 
	@Query(value = "insert into apiroadingstate(memId,site,state,num,totalNum) values (:memId,:site,:state,'0','0')", nativeQuery = true)
	void insertColumn(String memId, String site, String state);

	//처음 내역저장시킬때
	@Transactional
	@Modifying 
	@Query(value = "update apiroadingstate set state='1' where memId=:memId and site =:site", nativeQuery = true)
	int updateAs(String memId, String site);
	
	//다시불러오기 초기화 시킬때
	@Transactional
	@Modifying 
	@Query(value = "update apiroadingstate set state='0' where memId=:memId and site =:site", nativeQuery = true)
	int updateAsStart(String memId, String site);

	
	@Transactional
	@Modifying 
	@Query(value = "delete from apiroadingstate where memId = :memId and site = :temSite", nativeQuery = true)
	int deleteByMemId(String temSite, String memId);
	
	@Transactional
	@Modifying 
	@Query(value = "update apiroadingstate set num = :num where memId = :memId and site = :site", nativeQuery = true)
	void updateAsNum(String num, String memId, String site);
	
	@Transactional
	@Modifying 
	@Query(value = "update apiroadingstate set totalNum = :totalNum where memId = :memId and site = :site", nativeQuery = true)
	void updateAsTotalNum(String totalNum, String memId, String site);

}
