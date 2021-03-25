package gmc.rd.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.BankStateMentState;
import gmc.rd.report.entity.CandleStickState;

public interface CandleStickStateRepository extends JpaRepository<CandleStickState, Integer>{
	@Query(value = "select * from candlestickstate where site = :site and memId= :memId", nativeQuery = true)
	CandleStickState findByMemIdAndSite(String memId, String site);
	
	@Transactional
	@Modifying 
	@Query(value = "insert into candlestickstate(memId,site,state) values (:memId,:site,:state)", nativeQuery = true)
	void insertColumn(String memId, String site, String state);

	//처음 내역저장시킬때
	@Transactional
	@Modifying 
	@Query(value = "update candlestickstate set state='1' where memId=:memId and site =:site", nativeQuery = true)
	int updateAs(String memId, String site);
	
	//다시불러오기 초기화 시킬때
	@Transactional
	@Modifying 
	@Query(value = "update candlestickstate set state='0' where memId=:memId and site =:site", nativeQuery = true)
	int updateAsStart(String memId, String site);

	
	@Transactional
	@Modifying 
	@Query(value = "delete from candlestickstate where memId = :memId and site = :temSite", nativeQuery = true)
	int deleteByMemId(String temSite, String memId);
}
