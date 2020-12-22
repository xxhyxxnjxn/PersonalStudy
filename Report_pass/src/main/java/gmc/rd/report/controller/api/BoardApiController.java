package gmc.rd.report.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ResponseDto;
import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.service.BoardService;
import gmc.rd.report.service.ReportService;

@RestController
public class BoardApiController {
   
   @Autowired
   private BoardService boardService;
   @Autowired
   private ReportService reportService;
   
      @PostMapping("/api/board")
       public ResponseDto<Integer> save(@RequestBody Board board,@AuthenticationPrincipal PrincipalDetail principal) {
         boardService.글쓰기(board,principal.getUser());
          return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
       }
      @PostMapping("/log/board")
      public ResponseDto<Integer> saveLog(@RequestBody Report report,@AuthenticationPrincipal PrincipalDetail principal) {
         //boardService.logSave(report,principal.getUser());
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }
      @DeleteMapping("/api/board/{orderId}")
      public ResponseDto<Integer> deleteById(@PathVariable String orderId){
         boardService.상세삭제하기(orderId);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }
      @PutMapping("/api/board/{orderId}")
      public ResponseDto<Integer> update(@PathVariable String orderId,@RequestBody Board board){
         boardService.상세수정하기(orderId,board);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }

      @PutMapping("/api/report/bithumb/{orderId}")
      public ResponseDto<Integer> updateLog1(@PathVariable String orderId,@RequestBody Report report){

               boardService.updateLogbi(orderId,report);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }

      @PutMapping("/api/report/upbit/{orderId}")
      public ResponseDto<Integer> updateLog2(@PathVariable String orderId,@RequestBody Report2 report){

               boardService.updateLogup(orderId,report);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }

      @PutMapping("/api/report/coinone/{orderId}")
      public ResponseDto<Integer> updateLog3(@PathVariable String orderId,@RequestBody Report3 report){

               boardService.updateLogco(orderId,report);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }
      
      
    }