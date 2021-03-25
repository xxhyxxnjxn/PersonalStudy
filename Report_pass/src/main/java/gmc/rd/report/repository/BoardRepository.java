package gmc.rd.report.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
//findall -모든 것을 리턴함, findbyId
//DAO
//자동으로 bean등록 가능 : spring io를 할수잇음
//@Repository 생략가능
public interface BoardRepository extends JpaRepository<Board, Integer> {

	void deleteByOrderId(String orderId);

	Board findByOrderId(String orderId);

	
}
