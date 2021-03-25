package gmc.rd.report.controller;


import java.text.ParseException;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ResponseDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.RevenueStateRepository;
import gmc.rd.report.repository.UserRepository;
import gmc.rd.report.repository.VmRepository;

import gmc.rd.report.service.BithumbRevenueService;
import gmc.rd.report.service.CoinoneRevenueService;
import gmc.rd.report.service.RevenueLastestService;
import gmc.rd.report.service.RevenueService;
import gmc.rd.report.service.SiteFactory;
import gmc.rd.report.service.UpbitRevenueService;
import gmc.rd.report.service.VmService;


@RestController
public class TransactionController {

	@Autowired
	VmRepository vmRepository;

	@Autowired
	VmService vmService;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private ReportRepository2 reportRepository2;
	
	@Autowired
	private ReportRepository3 reportRepository3;
	
	@Autowired
	private BithumbRevenueService bithumbRevenueService;
	
	@Autowired
	private CoinoneRevenueService coinoneRevenueService;
	
	@Autowired
	private UpbitRevenueService upbitRevenueService;
	
	@Autowired
	RevenueStateRepository revenueStateRepository;
	
	@Autowired
	private SiteFactory siteFactory;

	int page_cnt = 1;
	List<Report2> report2;
	
	@PostMapping("/board/showAllRevenue") // 전체 수익률 계산
	public ResponseDto<Integer> showAllRevenue(@AuthenticationPrincipal PrincipalDetail principal,@RequestParam String site) throws ParseException { //throws Exception {
		String userId = principal.getUser().getMemId(); 
		revenueStateRepository.updatestate(userId, site, true);
		
		RevenueService revenueService = siteFactory.factoryReturn(site);
		revenueService.revenue(principal.getUser());
		
		
//		try {
//			
//			
//			User user = userRepository.findByMemId(userId).orElseThrow(IllegalArgumentException::new);
//	
//			if(site.equals("upbit")) {
//				List<Report2> groupCurrency = reportRepository2.groupByCurrency(userId);
//				List<Report2> orderByData = null;
//				for(int i=0;i<groupCurrency.size();i++) {
//					orderByData = reportRepository2.orderByTransactionDate(userId, groupCurrency.get(i).getCurrency());
//					System.out.println(orderByData.size());
//					upbitRevenueService.setAvgPrice_upbit("upbit", orderByData, user);
//				}
//			}else if(site.equals("bithumb")) {
//				List<Report> groupCurrency = reportRepository.groupByCurrency(userId);
//				List<Report> orderByData = null;
//				for(int i=0;i<groupCurrency.size();i++) {
//					orderByData = reportRepository.orderByTransactionDate(userId,groupCurrency.get(i).getCurrency());
//					System.out.println(orderByData.size());
//					bithumbRevenueService.setAvgPrice_bithumb("bithumb", orderByData, user);
//				}
//			}else if(site.equals("coinone")) {
//				List<Report3> groupCurrency = reportRepository3.groupByCurrency(userId);
//				List<Report3> orderByData = null;
//				for(int i=0;i<groupCurrency.size();i++) {
//					orderByData = reportRepository3.orderByTransactionDate(userId,groupCurrency.get(i).getCurrency());
//					System.out.println(orderByData.size());
//					coinoneRevenueService.setAvgPrice_coinone("coinone", orderByData, user);
//				}
//			}
			
			
			revenueStateRepository.updatestate(userId, site, false);
//		}catch(Exception e) {
//			revenueStateRepository.updatestate(userId, site, false);
//		}
		
			
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
			
	//		수익률 내는 part
	}
	
	@PostMapping("/board/showNewRevenue") // 새로 들어온 거래내역 수익률 계산
	public ResponseDto<Integer> showNewRevenue(@AuthenticationPrincipal PrincipalDetail principal,@RequestParam String site) throws ParseException { //throws Exception {
		
			
			String userId = principal.getUser().getMemId(); 
			
			revenueStateRepository.updatestate(userId, site, true);

			RevenueLastestService revenueLastestService = siteFactory.factoryReturnLastes(site);
			revenueLastestService.findByRowDto(principal.getUser(), site);
			
			List<ReportDto> groupByDto= revenueLastestService.groupByDto;
			List<ReportDto> findByRow = revenueLastestService.findByRowDto;
			List<ReportDto> findByNull = revenueLastestService.findByNullDto;
			
//			System.out.println("DDDDDDDDDDD"+groupByDto);
//			System.out.println("DDDDDDDDDDD"+findByRow);
//			System.out.println("DDDDDDDDDDD"+findByNull);
			
			if(groupByDto.isEmpty()) {
				
				if(findByRow == null && findByNull == null) {
					System.out.println("수익률 구할 값 없음");
				}
			}else {
				if((findByRow == null && findByNull != null) || (findByRow.isEmpty() && !findByNull.isEmpty())) {
					RevenueService revenueService = siteFactory.factoryReturn(site);
					revenueService.revenue(principal.getUser());
				}else if( (findByRow != null && findByNull != null) ||  (!findByRow.isEmpty() && !findByNull.isEmpty()) ) {
					revenueLastestService.revenue(principal.getUser());
				}
			}
			
			
			
			
//			try {
//				User user = userRepository.findByMemId(userId).orElseThrow(IllegalArgumentException::new);
//				
//				if(site.equals("upbit")) {
//					List<Report2> groupCurrency = reportRepository2.groupByCurrency(userId);
//					List<Report2> findByNull = null;
//					List<Report2> findByRow = null;
//					List<Report2> orderByData = null;
//					
//					for(int i=0;i<groupCurrency.size();i++) {
//						
//						findByNull = reportRepository2.findByNull(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("null : "+findByNull);
//						
//						findByRow = reportRepository2.findByLatestRow(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("row : "+findByRow);
//						
//						if(findByRow.isEmpty()) {
//							orderByData = reportRepository2.orderByTransactionDate(userId, groupCurrency.get(i).getCurrency());
//							upbitRevenueService.setAvgPrice_upbit("upbit", orderByData, user);
//						}else {
//							upbitRevenueService.setAvgPrice_upbit_latest("upbit", findByNull, findByRow,user);
//						}
//					}
//				}else if(site.equals("bithumb")) {
//					
//					List<Report> groupCurrency = reportRepository.groupByCurrency(userId);
//					List<Report> findByNull = null;
//					List<Report> findByRow = null;
//					List<Report> orderByData = null;
//					
//					for(int i=0;i<groupCurrency.size();i++) {
//						
//						findByNull = reportRepository.findByNull(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("null : "+findByNull);
//						
//						findByRow = reportRepository.findByLatestRow(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("row : "+findByRow);
//						
//						if(findByRow.isEmpty()) {
//							orderByData = reportRepository.orderByTransactionDate(userId,groupCurrency.get(i).getCurrency());
//							bithumbRevenueService.setAvgPrice_bithumb("bithumb", orderByData, user);
//						}else {
//							bithumbRevenueService.setAvgPrice_bithumb_latest("bithumb", findByNull, findByRow,user);
//						}
//					}
//				}else if(site.equals("coinone")) {
//					List<Report3> groupCurrency = reportRepository3.groupByCurrency(userId);
//					List<Report3> findByNull = null;
//					List<Report3> findByRow = null;
//					List<Report3> orderByData = null;
//					
//					for(int i=0;i<groupCurrency.size();i++) {
//						findByNull = reportRepository3.findByNull(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("null : "+findByNull);
//						
//						findByRow = reportRepository3.findByLatestRow(userId, groupCurrency.get(i).getCurrency());
//						System.out.println("row : "+findByRow);
//						
//						if(findByRow.isEmpty()) {
//							orderByData = reportRepository3.orderByTransactionDate(userId,groupCurrency.get(i).getCurrency());
//							coinoneRevenueService.setAvgPrice_coinone("coinone", orderByData, user);
//						}else {
//							coinoneRevenueService.setAvgPrice_coinone_latest("coinone", findByNull, findByRow,user);
//						}
//					}
//				}
				
				revenueStateRepository.updatestate(userId, site, false);
//			}catch(Exception e) {
//				revenueStateRepository.updatestate(userId, site, false);
//			}
			
			return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
//			거래내역 새로 들어왔을 때 구하는 수익률
	}

}
