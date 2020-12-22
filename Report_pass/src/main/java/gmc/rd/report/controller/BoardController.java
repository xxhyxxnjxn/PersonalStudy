package gmc.rd.report.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.service.ApiService;
import gmc.rd.report.service.BoardService;
import gmc.rd.report.service.TradeService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TradeService tradeService;
	@Autowired
	private ApiService apiService;
	
	@GetMapping({"","/"})
	public String index(Model model) {//컨트롤러에서 세션을 어떻게 찾는지?
		//model.addAttribute("boards",boardService.글목록());//data는 collection이라서 한 건 씩 들고와서 뿌려줌
		//request정보
		
		return "redirect:/getList";//viewResolver작동!! :  model에 포함되어있는 모든 아이들을 가지고 간다.
	}

	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping("/board/saveForm/{orderId}&{price}")
	public String saveForm(@PathVariable String orderId,Model model) {
		model.addAttribute("orderId",orderId);
		return "board/saveForm";
	}	
	
	@GetMapping("/board/{orderId}")
	public String findById(@PathVariable String orderId,Model model) {
		model.addAttribute(boardService.글상세보기(orderId));
		return "board/detail";
	}
	@GetMapping("/board/{orderId}/updateForm")
	public String updateForm(@PathVariable String orderId,Model model) {
		model.addAttribute(boardService.글상세보기(orderId));
		return "board/updateForm";
	}
	
	@GetMapping("/board/listForm")
	public String list(Model model) {//컨트롤러에서 세션을 어떻게 찾는지?
		model.addAttribute("boards",boardService.글목록());//data는 collection이라서 한 건 씩 들고와서 뿌려줌
		//request정보
		return "index";//viewResolver작동!! :  model에 포함되어있는 모든 아이들을 가지고 간다.
	}
	
	@GetMapping("/board/derailForm")
	public String detail(Model model) {//컨트롤러에서 세션을 어떻게 찾는지?
		model.addAttribute("boards",boardService.글목록());//data는 collection이라서 한 건 씩 들고와서 뿌려줌
		//request정보
		return "index";//viewResolver작동!! :  model에 포함되어있는 모든 아이들을 가지고 간다.
	}
	
	////////////////////////리포트 페이지 /////////////////////
	/*
	 * @GetMapping("/board/reportForm") public String reportForm(Model
	 * model,@AuthenticationPrincipal PrincipalDetail principal) {
	 * model.addAttribute("trades",tradeService.트레이딩목록(principal.getUser()));
	 * model.addAttribute("boards",boardService.글목록());///data는 collection이라서 한 건 씩
	 * 들고와서 뿌려줌 System.out.println("애슐리다아아아아앙아앙"+model.getAttribute("trades"));
	 * String temp = model.getAttribute("boards").toString();
	 * model.addAttribute("selectsCoins",tradeService.selectCoin(principal.getUser()
	 * ));
	 * 
	 * System.out.println(temp); if(temp=="[]") { model.addAttribute("boardsInit",
	 * "boardsInit");///data는 collection이라서 한 건 씩 들고와서 뿌려줌
	 * System.out.println(model); }else { model.addAttribute("boardsInit",
	 * "none");///data는 collection이라서 한 건 씩 들고와서 뿌려줌 System.out.println(model); }
	 * return "board/reportForm"; }
	 */
	
	
//	@GetMapping("/board/reportForm2")
//	public String reportForm2(Model model,@AuthenticationPrincipal PrincipalDetail principal) {
//		model.addAttribute("trades",tradeService.트레이딩목록2(principal.getUser()));
//		model.addAttribute("boards",boardService.글목록());///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//		System.out.println("애슐리다아아아아앙아앙"+model.getAttribute("trades"));
//		String temp = model.getAttribute("boards").toString();
//		model.addAttribute("selectsCoins",tradeService.selectCoin2(principal.getUser()));
//		
//		System.out.println(temp);
//		if(temp=="[]") {
//			model.addAttribute("boardsInit", "boardsInit");///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//			System.out.println(model);
//		}else {
//			model.addAttribute("boardsInit", "none");///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//			System.out.println(model);
//		}
//		return "board/reportForm2";
//	}
//	
//	
//	@GetMapping("/board/reportForm3")
//	public String reportForm3(Model model,@AuthenticationPrincipal PrincipalDetail principal) {
//		model.addAttribute("trades",tradeService.트레이딩목록3(principal.getUser()));
//		model.addAttribute("boards",boardService.글목록());///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//		System.out.println("애슐리다아아아아앙아앙"+model.getAttribute("trades"));
//		String temp = model.getAttribute("boards").toString();
//		model.addAttribute("selectsCoins",tradeService.selectCoin3(principal.getUser()));
//		
//		System.out.println(temp);
//		if(temp=="[]") {
//			model.addAttribute("boardsInit", "boardsInit");///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//			System.out.println(model);
//		}else {
//			model.addAttribute("boardsInit", "none");///data는 collection이라서 한 건 씩 들고와서 뿌려줌
//			System.out.println(model);
//		}
//		return "board/reportForm3";
//	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/board/reportForm/{site}")
	public String reportSite(Model model,@PathVariable String site, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site",site);
		model.addAttribute("selectsSites",apiService.selectSite(principal.getUser()));
		if(site.equals("bithumb")) {
		model.addAttribute("selectsCoins",tradeService.selectCoin(principal.getUser()));
		}else if(site.equals("upbit")) {
			model.addAttribute("selectsCoins",tradeService.selectCoin2(principal.getUser()));
			}else {
				model.addAttribute("selectsCoins",tradeService.selectCoin3(principal.getUser()));
			}
		
		return "board/reportFormSite";
	}
	//리스트 가져오기
	
	@ResponseBody
	@PostMapping("/board/reportForm/bithumb")
	public  List<Report> reportTrades1( @AuthenticationPrincipal PrincipalDetail principal,@RequestParam String orderby) {

	System.out.println(orderby);
		List<Report> list = tradeService.트레이딩목록(principal.getUser(),orderby);
		System.out.println(list);
		 return list;
		
	}
	@ResponseBody
	@PostMapping("/board/reportForm/upbit")
	public  List<Report2> reportTrades2( @AuthenticationPrincipal PrincipalDetail principal,@RequestParam String orderby) {
	
		List<Report2> list = tradeService.트레이딩목록2(principal.getUser(),orderby);
		
		return list;
		
	}
	@ResponseBody
	@PostMapping("/board/reportForm/coinone")
	public  List<Report3> reportTrades3( @AuthenticationPrincipal PrincipalDetail principal,@RequestParam String orderby) {
		
		List<Report3> list = tradeService.트레이딩목록3(principal.getUser(),orderby);
		
		return list;
		
	}
	//로그 가져오기
	@ResponseBody
	@PostMapping("/board/reportForm/{orderId}")
	public  void reportTradesupdatelog( HttpServletResponse response, @PathVariable String orderId, @AuthenticationPrincipal PrincipalDetail principal,String log) {
		
		boardService.updateLog(orderId,log);

		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//오더아이디에 맞는 레포트보드 가져오기
	@ResponseBody
	@GetMapping("/board/report/{orderId}")
	public String selectBoardByOrderId(@PathVariable String orderId){
	
		Board board = boardService.게시글(orderId);
		System.out.println(board);
		String result = "0";
		if(board==null) {
			result = "0";
		}else {
			result = "1";
		}
		return result;
	}
	@GetMapping("/board/saveForm/{orderId}")
	public String savefrom(@PathVariable String orderId,Model model) {
		model.addAttribute("orderId",orderId);
		return "board/saveForm";
	}	
	
	@GetMapping("/board/derailForm/{orderId}")
	public String derailform(@PathVariable String orderId,Model model) {
		model.addAttribute("board",boardService.게시글(orderId));//data는 collection이라서 한 건 씩 들고와서 뿌려줌
		//request정보
		return "board/detail";
	}	 
	
	
	
}
