package gmc.rd.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.BoardRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;

//스프링이 컴포넌트 스캔을 통해서 bean에 등록을 해줌 loC를 해준다.
@Service
public class BoardService {
   
   @Autowired
   private BoardRepository boardRepository;
   
   @Autowired
   private ReportRepository reportRepository;   
   @Autowired
   private ReportRepository2 reportRepository2;   
   @Autowired
   private ReportRepository3 reportRepository3;
   
   
   @Transactional
   public void  글쓰기(Board board,User user) {
      board.setUser(user);
      boardRepository.save(board);
   }
   
   @Transactional(readOnly = true)
   public List<Board> 글목록() {
      
      return boardRepository.findAll();
   }
   /*
   @Transactional
   public void logSave(Report report, User user) {
      report.setUser(user);
      boardRepository.save(report);
   }*/


   @Transactional(readOnly = true)
   public Board 글상세보기(String orderId) {
         return boardRepository.findByOrderId(orderId);
   }

   @Transactional
   public void 상세삭제하기(String orderId) {
       boardRepository.deleteByOrderId(orderId);      
   }
   @Transactional
   public void 상세수정하기(String orderId, Board requestBoard) {
      Board board  = boardRepository.findByOrderId(orderId); 
      board.setTitle(requestBoard.getTitle());
      board.setContent(requestBoard.getContent());
      // 해당 함수로 종료시 service가 종료될 때 트랜젝션이 종료됩니다. 이때 더티체킹 - 자동업데이트가 됩니다.
   }
   @Transactional
   public void updateLogbi(String orderId, Report requestReport) {
      Report report  = reportRepository.findByOrderId(orderId); // 영속화완료
      report.setLog(requestReport.getLog());
      
   }
   //하은 추가
   @Transactional
   public void updateLog(String orderId, String log) {
      Report report  = reportRepository.findByOrderId(orderId); // 영속화완료
      report.setLog(log);
      
   }
   @Transactional
   public Board 게시글(String orderId) {
      Board board  = boardRepository.findByOrderId(orderId); 
      return board;
   }
   @Transactional
   public void updateLogup(String orderId, Report2 requestReport) {
      Report2 report  = reportRepository2.findByOrderId(orderId); // 영속화완료
      report.setLog(requestReport.getLog());
      
   }
   @Transactional
   public void updateLogco(String orderId, Report3 requestReport) {
      Report3 report  = reportRepository3.findByOrderId(orderId); // 영속화완료
      report.setLog(requestReport.getLog());
      
   }





}