package gmc.rd.report.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ResponseDto;
import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.service.BoardService;
import gmc.rd.report.service.BithumbReportService;
import gmc.rd.report.service.TradeService;

@RestController
public class BoardApiController {
   
   @Autowired
   private BoardService boardService;
  
   @Autowired
	private TradeService tradeService;
   
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
      public ResponseDto<Integer> updateLog1(@PathVariable String orderId,@RequestBody Report report,@AuthenticationPrincipal PrincipalDetail principal){

               boardService.updateLogbi(orderId,report,principal.getUser().getMemId());
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }

      @PutMapping("/api/report/upbit/{orderId}")
      public ResponseDto<Integer> updateLog2(@PathVariable String orderId,@RequestBody Report2 report,@AuthenticationPrincipal PrincipalDetail principal){

               boardService.updateLogup(orderId,report,principal.getUser().getMemId());
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }

      @PutMapping("/api/report/coinone/{orderId}")
      public ResponseDto<Integer> updateLog3(@PathVariable String orderId,@RequestBody Report3 report,@AuthenticationPrincipal PrincipalDetail principal){
    	  		
               boardService.updateLogco(orderId,report,principal.getUser().getMemId());
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
      }
 	 @PutMapping("/board/reportForm/upbit/save")
     public ResponseDto<Integer> updatePriceup(@RequestParam String orderId,@RequestParam String price,@AuthenticationPrincipal PrincipalDetail principal){

		 tradeService.bankstatementup(orderId,price);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
     }

 	 @PutMapping("/board/reportForm/bithumb/save")
     public ResponseDto<Integer> updatePricebi(@RequestParam String orderId,@RequestParam String price,@AuthenticationPrincipal PrincipalDetail principal){

		 tradeService.bankstatementbi(orderId,price);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
     }
 	 @PutMapping("/board/reportForm/coinone/save")
     public ResponseDto<Integer> updatePriceco(@RequestParam String orderId,@RequestParam String price,@AuthenticationPrincipal PrincipalDetail principal){

		 tradeService.bankstatementco(orderId,price);
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
     }
      
      
    }