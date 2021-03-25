package gmc.rd.report.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.CountDto;
import gmc.rd.report.entity.ApiRoadingState;
import gmc.rd.report.entity.Board;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.RevenueState;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.RevenueStateRepository;
import gmc.rd.report.repository.TradeRepository;
import gmc.rd.report.repository.TradeRepository2;
import gmc.rd.report.repository.TradeRepository3;
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
	
	@Autowired
	RevenueStateRepository revenueStateRepository;
	
	@Autowired
	ApiRoadingStateRepository apiRoadingStateRepository;

	@Autowired
	TradeRepository2 tradeRepository2;
	@Autowired
	TradeRepository3 tradeRepository3;
	
	@Autowired
	TradeRepository tradeRepository;

	@GetMapping({ "", "/" })
	public String index(Model model) {// 컨트롤러에서 세션을 어떻게 찾는지?
		// model.addAttribute("boards",boardService.글목록());//data는 collection이라서 한 건 씩
		// 들고와서 뿌려줌
		// request정보

		return "redirect:/getList";// viewResolver작동!! : model에 포함되어있는 모든 아이들을 가지고 간다.
	}

	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}

	@GetMapping("/board/saveForm/{orderId}&{price}")
	public String saveForm(@PathVariable String orderId, Model model) {
		model.addAttribute("orderId", orderId);
		return "board/saveForm";
	}

	@GetMapping("/board/{orderId}/{site}")
	public String findById(@PathVariable String orderId, @PathVariable String site, Model model) {
		model.addAttribute(boardService.글상세보기(orderId));
		model.addAttribute("site", site);
		return "board/detail";
	}

	@GetMapping("/board/{orderId}/updateForm/{site}")
	public String updateForm(@PathVariable String orderId, @PathVariable String site, Model model) {
		model.addAttribute(boardService.글상세보기(orderId));
		model.addAttribute("site", site);
		return "board/updateForm";
	}

	@GetMapping("/board/listForm")
	public String list(Model model) {// 컨트롤러에서 세션을 어떻게 찾는지?
		model.addAttribute("boards", boardService.글목록());// data는 collection이라서 한 건 씩 들고와서 뿌려줌
		// request정보
		return "index";// viewResolver작동!! : model에 포함되어있는 모든 아이들을 가지고 간다.
	}

	@GetMapping("/board/derailForm")
	public String detail(Model model) {// 컨트롤러에서 세션을 어떻게 찾는지?
		model.addAttribute("boards", boardService.글목록());// data는 collection이라서 한 건 씩 들고와서 뿌려줌
		// request정보
		return "index";// viewResolver작동!! : model에 포함되어있는 모든 아이들을 가지고 간다.
	}

	//////////////////////// 리포트 페이지 /////////////////////
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
	public String reportSite(Model model, @PathVariable String site, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site", site);
		model.addAttribute("selectsSites", apiService.selectSite(principal.getUser()));

		if (site.equals("bithumb")) {
			model.addAttribute("selectsCoins", tradeService.selectCoin(principal.getUser()));
			model.addAttribute("statementList", tradeService.bankstatement0(principal.getUser()));
		} else if (site.equals("upbit")) {
			model.addAttribute("selectsCoins", tradeService.selectCoin2(principal.getUser()));
			model.addAttribute("statementList", tradeService.bankstatement02(principal.getUser()));
			
		} else {
			model.addAttribute("selectsCoins", tradeService.selectCoin3(principal.getUser()));
			model.addAttribute("statementList", tradeService.bankstatement03(principal.getUser()));
			
		}
		
		model.addAttribute("state", revenueStateRepository.findByMemIdAndSite(principal.getUser().getMemId(), site));

		return "board/reportFormSite";
	}
	// 리스트 가져오기
	
	
	//// 가격 입력
	@GetMapping("/board/reportForm/{site}/bankstatement")
	public String reportSite2(Model model, @PathVariable String site, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site", site);

		
		if (site.equals("bithumb")) {
			model.addAttribute("statementList", tradeService.bankstatement(principal.getUser()));
		} else if (site.equals("upbit")) {
			
			//model.addAttribute("statementList", tradeService.bankstatement2(principal.getUser(),orderby, type));
			//model.addAttribute("statementList", tradeService.bankstatement2(principal.getUser()));
			
		} else {
			model.addAttribute("statementList", tradeService.bankstatement3(principal.getUser()));
			
		}

		return "board/bankstatement";
	}
	
	
	//// 가격 입력

	@ResponseBody
	@PostMapping("/board/reportForm/bithumb/bankstatement")
	public List<Report> reportSitebi(Model model, @RequestParam String site, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site", site);

			List<Report> list =  tradeService.bankstatement(principal.getUser());


		return list;
	}
	
	@ResponseBody
	@PostMapping("/board/reportForm/bithumb/bankstatement/candleStick")
	public void showCandleStick(Model model, @RequestParam String date, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {

		System.out.println(date);

	}
	//// 가격 입력
	@ResponseBody
	@PostMapping("/board/reportForm/upbit/bankstatement")
	public List<Report2> reportSiteup(Model model, @RequestParam String site, @RequestParam String orderby,  @RequestParam String type,  @RequestParam String priceSelect  ,HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site", site);
			List<Report2> list =  tradeService.bankstatement2(principal.getUser(),orderby, type,priceSelect);
		return list;
	}
	//// 가격 입력

	@ResponseBody
	@PostMapping("/board/reportForm/coinone/bankstatement")
	public List<Report3> reportSiteco(Model model, @RequestParam String site, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("site", site);

			List<Report3> list =  tradeService.bankstatement3(principal.getUser());


		return list;
	}
	


	@ResponseBody
	@PostMapping("/board/reportForm/state")
	public RevenueState state(	@AuthenticationPrincipal PrincipalDetail principal,	@RequestParam String site){


		RevenueState revenueState= revenueStateRepository.findByMemIdAndSite(principal.getUser().getMemId(), site);
		ApiRoadingState apiRoadingState = apiRoadingStateRepository.findByMemIdAndSite(principal.getUser().getMemId(), site);
		
		System.err.println(revenueState.isState());
		System.err.println(apiRoadingState.getState());
		
		
		//true이면 못누르게  						//0이면 못르게
		if(revenueState.isState() == false && apiRoadingState.getState().equals("1")) {
			revenueState.setState(false);
		}else {
				revenueState.setState(true);
				
		}
		
		return revenueState;

	}
	

	@ResponseBody
	@PostMapping("/board/reportForm/bithumb")
	public Page<Report> reportTrades1(@AuthenticationPrincipal PrincipalDetail principal, @RequestParam String currency,
			@RequestParam String orderby, @RequestParam String start, @RequestParam String end,@RequestParam String type,
			@PageableDefault(size = 50, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable page)
//			@SortDefault.SortDefaults({@SortDefault(sort = "transactionDate",direction = Sort.Direction.DESC),@SortDefault(sort = "bidAccUnits",direction = Sort.Direction.DESC),@SortDefault(sort = "incomeCal",direction = Sort.Direction.DESC)})
//			Pageable page)
			throws Exception {

		if (orderby.equals("ASC")) {
			page = PageRequest.of(page.getPageNumber(), 50, Sort.by("transactionDate"));
		}
		Page<Report> list = tradeService.트레이딩목록1(principal.getUser(), page, currency, start, end,type);
		return list;

	}
	
	@ResponseBody
	@PostMapping("/board/reportForm/bithumb/rownum")
	public List<String> bithumbRownum(@AuthenticationPrincipal PrincipalDetail principal,@RequestParam String currency, @RequestParam String orderby, @RequestParam String start,
			@RequestParam String end,@RequestParam String type)
			throws Exception {
		
		long count = tradeService.findCount(principal.getUser(),currency, start, end, type);

		List<String> list = new ArrayList<>();
		list.add(String.valueOf(count));

		return list;

	}
	
	@ResponseBody
	@PostMapping("/board/reportForm/bithumb/dateCount")
	public List<String> dateCountBithumb(@AuthenticationPrincipal PrincipalDetail principal)
			throws Exception {

		List<String> transactionDate = new ArrayList<>();
		
		String max = tradeRepository.findByMaxTransaction(principal.getUser().getMemId());
		String min = tradeRepository.findByMinTransaction(principal.getUser().getMemId());
		
		transactionDate.add(max);
		transactionDate.add(min);

		return transactionDate;

	}
	


	@ResponseBody
	@PostMapping("/board/reportForm/upbit")
	public Page<Report2> reportTrades2(@AuthenticationPrincipal PrincipalDetail principal,
			@RequestParam String currency, @RequestParam String orderby, @RequestParam String start,
			@RequestParam String end,@RequestParam String type,
			@PageableDefault(size = 50) 
			@SortDefault.SortDefaults({@SortDefault(sort = "transactionDate",direction = Sort.Direction.DESC),@SortDefault(sort = "bidAccUnits",direction = Sort.Direction.DESC),@SortDefault(sort = "incomeCal",direction = Sort.Direction.DESC)})
			Pageable page)
			//@PageableDefault(size = 50, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable page)
			throws Exception {       

		if (orderby.equals("ASC")) {
			page = PageRequest.of(page.getPageNumber(), 50, Sort.by("transactionDate"));
		}

		Page<Report2> list = tradeService.트레이딩목록2(principal.getUser(), page, currency, start, end,type);
		return list;

	}

	@ResponseBody
	@PostMapping("/board/reportForm/upbit/rownum")
	public List<String> upbitRownum(@AuthenticationPrincipal PrincipalDetail principal,@RequestParam String currency, @RequestParam String orderby, @RequestParam String start,
			@RequestParam String end,@RequestParam String type)
			throws Exception {

		long count = tradeService.findCount2(principal.getUser(),currency, start, end, type);

		
		List<String> list = new ArrayList<>();
		list.add(String.valueOf(count));
		return list;

	}
	
	@ResponseBody
	@PostMapping("/board/reportForm/upbit/dateCount")
	public List<String> dateCountUpbit(@AuthenticationPrincipal PrincipalDetail principal)
			throws Exception {

		List<String> transactionDate = new ArrayList<>();
		
		String max = tradeRepository2.findByMaxTransaction(principal.getUser().getMemId());
		String min = tradeRepository2.findByMinTransaction(principal.getUser().getMemId());
		
		transactionDate.add(max);
		transactionDate.add(min);
		
		List<String> type = new ArrayList<>();
		
		type = tradeRepository2.findByType(principal.getUser().getMemId());
				
		transactionDate.addAll(type);
		
		return transactionDate;

	}
	
	@ResponseBody
	@PostMapping("/board/reportForm/coinone")
	public Page<Report3> reportTrades3(@AuthenticationPrincipal PrincipalDetail principal,
			@RequestParam String currency, @RequestParam String orderby, @RequestParam String start,
			@RequestParam String end,@RequestParam String type,
			@PageableDefault(size = 50) 
			@SortDefault.SortDefaults({@SortDefault(sort = "transactionDate",direction = Sort.Direction.DESC),@SortDefault(sort = "bidAccUnits",direction = Sort.Direction.DESC),@SortDefault(sort = "incomeCal",direction = Sort.Direction.DESC)})
			Pageable page)
			//@PageableDefault(size = 50, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable page)
			throws Exception {

		if (orderby.equals("ASC")) {
			page = PageRequest.of(page.getPageNumber(), 50, Sort.by("transactionDate"));
		}
		System.err.println(page);
		Page<Report3> list = tradeService.트레이딩목록3(principal.getUser(), page, currency, start, end, type);

		return list;

	}

	@ResponseBody
	@PostMapping("/board/reportForm/coinone/rownum")
	public List<String> coinoneRownum(@AuthenticationPrincipal PrincipalDetail principal,@RequestParam String currency, @RequestParam String orderby, @RequestParam String start,
			@RequestParam String end,@RequestParam String type)
			throws Exception {

		long count = tradeService.findCount3(principal.getUser(),currency, start, end, type);

		List<String> list = new ArrayList<>();
		list.add(String.valueOf(count));

		return list;

	}
	@ResponseBody
	@PostMapping("/board/reportForm/coinone/dateCount")
	public List<String> dateCountCoinone(@AuthenticationPrincipal PrincipalDetail principal)
			throws Exception {

		List<String> transactionDate = new ArrayList<>();
		
		String max = tradeRepository3.findByMaxTransaction(principal.getUser().getMemId());
		String min = tradeRepository3.findByMinTransaction(principal.getUser().getMemId());
		
		transactionDate.add(max);
		transactionDate.add(min);

		return transactionDate;

	}
	// 로그 가져오기
	@ResponseBody
	@PostMapping("/board/reportForm/{orderId}")
	public void reportTradesupdatelog(HttpServletResponse response, @PathVariable String orderId,
			@AuthenticationPrincipal PrincipalDetail principal, String log) {

		boardService.updateLog(orderId, log, principal.getUser().getMemId());

		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 오더아이디에 맞는 레포트보드 가져오기
	@ResponseBody
	@GetMapping("/board/report/{orderId}")
	public String selectBoardByOrderId(@PathVariable String orderId) {

		Board board = boardService.게시글(orderId);
		System.out.println(board);
		String result = "0";
		if (board == null) {
			result = "0";
		} else {
			result = "1";
		}
		return result;
	}

	@GetMapping("/board/saveForm/{orderId}/{site}")
	public String savefrom(@PathVariable String orderId, @PathVariable String site, Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("site", site);
		return "board/saveForm";
	}

	@GetMapping("/board/derailForm/{orderId}")
	public String derailform(@PathVariable String orderId, Model model) {
		model.addAttribute("board", boardService.게시글(orderId));// data는 collection이라서 한 건 씩 들고와서 뿌려줌
		// request정보
		return "board/detail";
	}

	@RequestMapping("/getrevenue/upbit")
	public void reportTradesupdate(@AuthenticationPrincipal PrincipalDetail principal) throws ParseException {

//		List<Report2> getlist = reportRepository2.findByabc(principal.getUser().getMemId());
//		reportService.setAvgPrice_upbit(getlist);

	}

}
