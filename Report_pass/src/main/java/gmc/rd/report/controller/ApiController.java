package gmc.rd.report.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;
import gmc.rd.report.api.upbit.vo.Withdraws;
import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.ApiRoadingState;
import gmc.rd.report.entity.BankStateMentState;
import gmc.rd.report.entity.CandleStickState;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.RevenueState;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CandleStickStateRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.RevenueStateRepository;
import gmc.rd.report.repository.UserRepository;
import gmc.rd.report.service.ApiService;
import gmc.rd.report.service.BithumbReportService;
import gmc.rd.report.service.CoinoneReportService;
import gmc.rd.report.service.UpbitReportService;
import gmc.rd.report.service.UserService;
import gmc.rd.report.service.VmService;

@Controller
public class ApiController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UpbitApiService upbitApiService;
	@Autowired
	private VmService vmService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ApiRoadingStateRepository apiRoadingRepository;
	@Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;
	@Autowired
	private CandleStickStateRepository candleStickStateRepository;
	@Autowired
	private RevenueStateRepository revenueStateRepository;
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ReportRepository2 reportRepository2;
	@Autowired
	private ReportRepository3 reportRepository3;
	@Autowired
	private ApiRepository apiRepository;

	@Autowired
	private ApiService apiService;
	
	@Qualifier("bithumbReportServiceInsert")
	@Autowired
	private BithumbReportService bithumbReportServiceInsert;
	
	@Qualifier("bithumbReportServiceUpdate")
	@Autowired
	private BithumbReportService bithumbReportServiceUpdate;
	
	@Qualifier("upbitReportServiceInsert")
	@Autowired
	private UpbitReportService upbitReportServiceInsert;
	
	@Qualifier("upbitReportServiceUpdate")
	@Autowired
	private UpbitReportService upbitReportServiceUpdate;
	
	@Autowired
	private CoinoneReportService coinoneReportService;
	@Autowired
	private BithumbApiService bithumbApiService;
	
	int page_cnt = 1;
	List<Report2> report2;
	List<Withdraws> withdraws;
	List<Deposit> deposit;
	
	ModelAndView mv = new ModelAndView();

	// apikey 생성 페이지
	@GetMapping("/apikey")
	public ModelAndView ApiKey(@AuthenticationPrincipal PrincipalDetail principal) {

		mv.addObject("memId", principal.getUsername());
		mv.setViewName("mem/apikey");
		return mv;
	}

	// apikey 생성
	@PostMapping("/apikey/insert")
	public void insertApiKey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,
			ApiDto apiDto) throws Exception {
		apiDto.setMemId(principal.getUsername());
		apiService.insertApiKey(apiDto);
		//0이면 돌고있는 중
		apiRoadingRepository.insertColumn(principal.getUsername(),apiDto.getSite(),"0");
		bankStateMentStateRepository.insertColumn(principal.getUsername(),apiDto.getSite(),"0");
		candleStickStateRepository.insertColumn(principal.getUsername(),apiDto.getSite(),"0");
		try {

			User user = userService.selectMemId(principal.getUser());

			if (apiDto.getSite().equals("upbit")) {
				Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");
				
				while (true) {
					report2 = upbitReportServiceInsert.selectReport("upbit", page_cnt, user);


					if (report2 == null) {
						page_cnt = 1;
						break;
					} else {

						page_cnt++;
						if(page_cnt%6==0) {
							Thread.sleep(1500);
						}
					}
				}
				while (true) {
					//report2 = upbitReportService.selectReport2("upbit", page_cnt, user);
					report2 = upbitReportServiceInsert.selectReport2("upbit", page_cnt, user);

					if (report2 == null) {
						page_cnt = 1;
						break;
					} else {

						page_cnt++;
						if(page_cnt%6==0) {
							Thread.sleep(1500);
						}
					}
				}
				
				int updateAsAmount =  apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				List<MarketAll> marketAll = upbitApiService.getMarketAll();
				bankStateMentStateRepository.updateAsTotalNum(String.valueOf(marketAll.size()),user.getMemId(),apiDto.getSite());
				for (int i = 0; i < marketAll.size(); i++) {
					
					bankStateMentStateRepository.updateAsNum(String.valueOf(i),user.getMemId(),apiDto.getSite());
					
					String currencyselect = marketAll.get(i).getMarket();
//					deposit = upbitReportService.getDeposit(principal.getUsername(), currencyselect);
//					withdraws = upbitReportService.getWithdraw(principal.getUsername(), currencyselect);
					deposit = upbitReportServiceInsert.getDeposit(principal.getUsername(), currencyselect,user,api);
					withdraws = upbitReportServiceInsert.getWithdraw(principal.getUsername(), currencyselect,user,api);
				}
				
				int updateAsAmount_bank =  bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				upbitReportServiceInsert.selectCandleStick(user);
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());

			} else if (apiDto.getSite().equals("bithumb")) {

				//user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
				//user = userService.selectMemId(principal.getUser());
				vmService.getVmBithumb(principal.getUsername(), user); // 거래내역, 입출금 조회

				int updateAsAmount_bank =  bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				bithumbReportServiceInsert.selectCandleStick(user); // 입출금 가격 candlestick 조회
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				

			} else if (apiDto.getSite().equals("coinone")) {

				vmService.getVmCoinone(user);

				coinoneReportService.selectCandleStcik(user);
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
			}
			
			
			
			response.getWriter().print("");

			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	// apikey 생성
	@GetMapping("/vm")
	public void test(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response) throws Exception {
		String result = bithumbApiService.getCandleStick("GAS","6h");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode treeNode = mapper.readTree(result);
		
	}
	
	@PostMapping("/board/refresh")
	public void refresh(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,ApiDto apiDto, @RequestParam String site) throws Exception {
		
	    String memId = principal.getUsername();
	    Api apidto =  apiRepository.findByMemIdAndSite(memId,site);
	    
		try {
			//User user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
			User user = userService.selectMemId(principal.getUser());
			int val = 0;
			if(site.equals("upbit")) {
				Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");
				
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				
				reportRepository2.deleteByMemId(memId);
				while (true) {
					//report2 = upbitReportService.selectReport("upbit", page_cnt, user);
					report2 = upbitReportServiceInsert.selectReport("upbit", page_cnt, user);

					if (report2 == null) {
						page_cnt = 1;
						break;
					} else {

						page_cnt++;
						if(page_cnt%6==0) {
							Thread.sleep(1500);
						}
					}
				}
				while (true) {
					//report2 = upbitReportService.selectReport2("upbit", page_cnt, user);
					report2 = upbitReportServiceInsert.selectReport2("upbit", page_cnt, user);

					if (report2 == null) {
						page_cnt = 1;
						break;
					} else {
						page_cnt++;
						if(page_cnt%6==0) {
							Thread.sleep(1500);
						}
					}
				}
				
				 apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
				 
				
				 
				List<MarketAll> marketAll = upbitApiService.getMarketAll();
				bankStateMentStateRepository.updateAsTotalNum(String.valueOf(marketAll.size()),user.getMemId(),apiDto.getSite());
				for (int i = 0; i < marketAll.size(); i++) {
					
					bankStateMentStateRepository.updateAsNum(String.valueOf(i),user.getMemId(),apiDto.getSite());
					
					String currencyselect = marketAll.get(i).getMarket();
					Thread.sleep(1000);
					//deposit = upbitReportService.getDeposit(principal.getUsername(), currencyselect);
					deposit = upbitReportServiceInsert.getDeposit(principal.getUsername(), currencyselect,user,api);
					//Thread.sleep(1000);
					//withdraws = upbitReportService.getWithdraw(principal.getUsername(), currencyselect);
					withdraws = upbitReportServiceInsert.getWithdraw(principal.getUsername(), currencyselect,user,api);
				}
				bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());

				upbitReportServiceInsert.selectCandleStick(user);
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
			}else if(site.equals("bithumb")) {
				
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				
				reportRepository.deleteByMemId(memId);
				System.err.println("bithumb 거래소 "+memId+"회원"+val+"개 삭제");
				
				//user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
				vmService.getVmBithumb(principal.getUsername(), user);
				//apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
				bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				bithumbReportServiceInsert.selectCandleStick(user); // 입출금 가격 candlestick 조회
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				
				
				
			}else if(site.equals("coinone")) {
				
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				
				reportRepository3.deleteByMemId(memId);
				 
				vmService.getVmCoinone(user);
				
				coinoneReportService.selectCandleStcik(user);
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
//				apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
//				
//				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
//				coinoneReportService.selectCandleStcik(user);
//				bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				
				
			}
		
			ApiRoadingState as =  apiRoadingRepository.findByMemIdAndSite(principal.getUsername(),site);
			response.getWriter().print("");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	@PostMapping("/board/updateLately")
	public void updateLately(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,ApiDto apiDto, @RequestParam String site) throws Exception {
		
		String memId = principal.getUsername();
		Api apidto =  apiRepository.findByMemIdAndSite(memId,site);
		
		try {
			//User user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
			User user = userService.selectMemId(principal.getUser());

			if(site.equals("upbit")) {
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				
					//user = userRepository.findByMemId1(user.getMemId());
					Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");
					if(api == null) {
						System.out.println(user.getMemId() + " : apikey 없음" );
					}else {
						while (true) {
							//report2 = upbitReportService.selectReportUpdate("upbit", page_cnt, user);
							report2 = upbitReportServiceUpdate.selectReport("upbit", page_cnt, user);
							if (report2 == null) {
								page_cnt = 1;
								break;
							} else {
								if(page_cnt == 3) {
									page_cnt = 1;
									break;
									
								}else {
									
									page_cnt++;
								}
							}
						}
					}
					apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
					
					
					List<MarketAll> marketAll = upbitApiService.getMarketAll();
					bankStateMentStateRepository.updateAsTotalNum(String.valueOf(marketAll.size()),user.getMemId(),apiDto.getSite());
					for (int i = 0; i < marketAll.size(); i++) {
						
						bankStateMentStateRepository.updateAsNum(String.valueOf(i),user.getMemId(),apiDto.getSite());
						
						String currencyselect = marketAll.get(i).getMarket();
						System.out.println("입금조회시작");
						Thread.sleep(1000);
						//deposit = upbitReportService.getDepositUpdate(principal.getUsername(), currencyselect);
						deposit = upbitReportServiceUpdate.getDeposit(principal.getUsername(), currencyselect,user,api);
						System.out.println("출금조회시작");
						//Thread.sleep(1000);
						//withdraws = upbitReportService.getWithdrawUpdate(principal.getUsername(), currencyselect);
						withdraws = upbitReportServiceUpdate.getWithdraw(principal.getUsername(), currencyselect,user,api);
					}
					bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
					
					
					upbitReportServiceUpdate.selectCandleStick(user);
					candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
					
			}else if(site.equals("bithumb")) {
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				
				//user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
				vmService.getVmBithumbUpdate(principal.getUsername(), user); // 거래내역, 입출금 내역 조회
//				apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
				bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				bithumbReportServiceUpdate.selectCandleStick(user); // 입출금 가격 candlestick 조회
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				

			}else if(site.equals("coinone")) {
				
//				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
//				 
//				vmService.getVmCoinone(user);
//				
//				apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
//				
//				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
//				coinoneReportService.selectCandleStcikUpdate(user);
//				bankStateMentStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				bankStateMentStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				candleStickStateRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
				 
				vmService.getVmCoinoneUpdate(user);
				
				coinoneReportService.selectCandleStcikUpdate(user);
				candleStickStateRepository.updateAs(user.getMemId(),apiDto.getSite());
				
			}
						
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@PostMapping("/board/updateLately/coinone")
//	public void updateLately_coinone(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,ApiDto apiDto, @RequestParam String site, @RequestParam String currency) throws Exception {
//		
//		String memId = principal.getUsername();
//		Api apidto =  apiRepository.findByMemIdAndSite(memId,site);
//		
//		try {
//			//User user = userRepository.findByMemId(principal.getUsername()).orElseThrow(IllegalArgumentException::new);
//			User user = userService.selectMemId(principal.getUser());
//
//
//				apiRoadingRepository.updateAsStart(principal.getUsername(),apiDto.getSite());
//
//				coinoneReportService.selectReportUpdate(user, currency.toUpperCase());
//				apiRoadingRepository.updateAs(user.getMemId(),apiDto.getSite());
//
//						
//
//			response.getWriter().print("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	// apikey 업데이트 페이지
	@PostMapping("/apikey")
	public ModelAndView updateApiKey(ApiDto apiDto) {

		apiDto = apiService.selectApiKey(apiDto);

		mv.addObject("apiDto", apiDto);
		mv.setViewName("mem/apikeyupdate");
		return mv;
	}

	// apikey 생성
	@PostMapping("/apikey/update")
	public void updateApiKey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,
			ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
		apiService.updateApiKey(apiDto);

		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/apikey/site")
	public void selectApiKey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,
			ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
		apiDto = apiService.selectApiKey(apiDto);
		try {
			response.getWriter().print(apiDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/apikey/auth")
	public void authApikey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,
			ApiDto apiDto) throws Exception {
		apiDto.setMemId(principal.getUsername());
		String result = apiService.authApikey(apiDto);

		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Transactional
	@PostMapping("/confirm/apiState")
	public void authApikey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,@RequestParam String site) {
		

		ApiRoadingState as =  apiRoadingRepository.findByMemIdAndSite(principal.getUsername(),site);
		
		try {
			response.getWriter().print(as.getState());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@ResponseBody
	@PostMapping("/board/apiRoadingState/{site}")
	public ApiRoadingState apiRoadingState(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		ApiRoadingState as =  apiRoadingRepository.findByMemIdAndSite(principal.getUsername(),site);
	//	System.out.println("DDDDDDDDDDDD"+as);
		return as;

	}
	
	@ResponseBody
	@PostMapping("/board/BankStateMentState/{site}")
	public BankStateMentState BankStateMentState(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		BankStateMentState as =  bankStateMentStateRepository.findByMemIdAndSite(principal.getUsername(),site);
	//	System.out.println("DDDDDDDDDDDD"+as);
		return as;

	}
	
	@ResponseBody
	@PostMapping("/board/CandleStickState/{site}")
	public CandleStickState CandleStickState(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		CandleStickState as =  candleStickStateRepository.findByMemIdAndSite(principal.getUsername(),site);
	//	System.out.println("DDDDDDDDDDDD"+as);
		return as;

	}
	
	@ResponseBody
	@PostMapping("/board/RevenueState/{site}")
	public RevenueState RevenueState(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		RevenueState as =  revenueStateRepository.findByMemIdAndSite(principal.getUsername(),site);
	//	System.out.println("DDDDDDDDDDDD"+as);
		return as;

	}
	
	@ResponseBody
	@PostMapping("/board/apiRoadingStateError/{site}")
	public int apiRoadingStateError(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		 int x = apiRoadingRepository.updateAs(principal.getUser().getMemId(),site);
		return x;

	}
	
	@ResponseBody
	@PostMapping("/board/BankStateMentStateError/{site}")
	public int BankStateMentStateError(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		int x= bankStateMentStateRepository.updateAs(principal.getUser().getMemId(),site);
		return x;

	}
	
	@ResponseBody
	@PostMapping("/board/CandleStickStateError/{site}")
	public int CandleStickStateError(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		int x= candleStickStateRepository.updateAs(principal.getUser().getMemId(),site);
		return x;

	}
	
	@ResponseBody
	@PostMapping("/board/RevenueStateError/{site}")
	public int RevenueStateError(HttpServletResponse response, @PathVariable String site,
			@AuthenticationPrincipal PrincipalDetail principal) {
		int x = revenueStateRepository.updatestate(principal.getUser().getMemId(), site, false);
		return x;

	}
//	@RequestMapping("//mem/apikey/")
//	public void scriptlist(HttpServletRequest request, HttpServletResponse response) throws Exception{
//	JSONArray jsonArray = scriptService.getScriptList();	
//	response.getWriter().print(jsonArray);
//	}

}