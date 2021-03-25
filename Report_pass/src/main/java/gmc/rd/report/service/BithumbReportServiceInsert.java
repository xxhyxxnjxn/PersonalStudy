package gmc.rd.report.service;

import java.lang.reflect.Type;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDtoMapper;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ReportRepository;

@Service
public class BithumbReportServiceInsert implements BithumbReportService{

	// 빗썸
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private BithumbApiService bithumbApiService;

	@Autowired
	private ApiRepository apiRepository;

	   
	@Transactional
	@Override
	public void selectReport(String userId, String currency ,User user) throws Exception {
		// thread에서는 user로 for문 돌리면 됨
		// 여기서는 로그인한 유저정보를 받아와야한다.
		Gson gson = new Gson();

		List<BithumbTransactionVo> bithumbTransaction = null;

		List<ReportDto> reportDto = null;

		List<Report> report = null;
		List<Report> bithumbReport = null;

		String jsonStr = null;
		Api api = apiRepository.findByMemIdAndSite(userId, "bithumb"); // 로그인한 유저 아이디 들고와야함
		ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
		if (apiDto == null) {
			System.out.println("빗썸 api 키 없음");
			
		} else {

			//for (VmDto result : vmDto) {

				System.out.println("coin : " + currency);
				for (int j = 0;;) {
					String offSet = String.valueOf(j);
					bithumbTransaction = bithumbApiService.getUserTransaction(api.getApiKey(), api.getSecretKey(),currency,offSet);
					
					if(bithumbTransaction==null) {
						break;
					}else {
						jsonStr = new ObjectMapper().writeValueAsString(bithumbTransaction);

						Type listType = new TypeToken<List<ReportDto>>() {
						}.getType();
						reportDto = gson.fromJson(jsonStr, listType);

						report = ReportDtoMapper.INSTANCE.toEntity(reportDto);
						
						String before_orderId = "beforeOrderId";
						String orderId = null;
						int num=0;
						
						for (int i = 0; i < report.size(); i++) {
							// OrderId설정 = timestamp+currency+units+type -> 나중에 거래 내역 다시 불러와서 저장할 때 중복저장 안되게
							// 함
							
							if (report.get(i).getType().equals("4")) {
								report.get(i).setPrice("0.0");
								if (report.get(i).getUnits().contains("+")) {
									String str = report.get(i).getUnits();
									str = str.replace("+ ", "");
									report.get(i).setUnits(str);
								}

							} else if (report.get(i).getType().equals("5")) {
								report.get(i).setPrice("0.0");
								if (report.get(i).getUnits().contains("-")) {
									String str = report.get(i).getUnits();
									str = str.replace("- ", "");
									report.get(i).setUnits(str);
								}
							} 
							
							DecimalFormat form = new DecimalFormat("0.00000000");
						    double dNumber = Double.valueOf(report.get(i).getUnits());
						    report.get(i).setUnits(form.format(dNumber));

						    orderId = (report.get(i).getTransactionDate()).substring(0, 10) + report.get(i).getCurrency() + report.get(i).getType() + report.get(i).getUnits().replace(".", "")+userId;
						    
						    if(before_orderId.equals(orderId)) {
								num++;
							}else {
								num=0;
							}
							
							report.get(i).setOrderId(orderId+num);
							
							before_orderId = orderId;
							
							String tempType = report.get(i).getType();
							if(tempType.equals("1")) {
								report.get(i).setType("매수");
							}else if(tempType.equals("2")) {
								report.get(i).setType("매도");								
							}else if(tempType.equals("3")) {
								report.get(i).setType("출금 중");								
							}else if(tempType.equals("4")) {
								report.get(i).setType("입금");								
							}else if(tempType.equals("5")) {
								report.get(i).setType("출금");								
							}else if(tempType.equals("9")) {
								report.get(i).setType("KRW 입금 중");								
							}									
							report.get(i).setSite("bithumb"); // 사이트 설정
							report.get(i).setUser(user); // 사이트 설정
							
							
						}
						System.err.println(report.size());
						//bithumbReport = setBithumbData(report,user);
						
						
						reportRepository.saveAll(report);
						
						//reportNumRepository.insertOrderId(report.get(i));
						
						
					}
					j+=50;
					
				}
			 
			//}
		}
		//return null;
	}
	
	@Transactional
	@Override
	public void selectCandleStick(User user) throws JsonProcessingException, ParseException, Exception{
		List<Report> groupByCurrencyBankStateMent = reportRepository.groupByCurrencyBankStateMent(user.getMemId());

		
		for(int i=0;i<groupByCurrencyBankStateMent.size();i++) {
//			System.out.println(groupByCurrencyBankStateMent.get(i).getCurrency());
			String result = bithumbApiService.getCandleStick(groupByCurrencyBankStateMent.get(i).getCurrency(),"1h");
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode treeNode = mapper.readTree(result);
			
//			System.out.println(treeNode.get("data").size());
			
			List<Report> findByBankStateMent = reportRepository.findByBankStateMent(user.getMemId(), groupByCurrencyBankStateMent.get(i).getCurrency());
			
			
			int candleNum = 0;
			
			for (int j = 0; j < findByBankStateMent.size(); j++) {
				String str = findByBankStateMent.get(j).getTransactionDate();
				String resultDate = str.substring(0, str.length()-3);
				
//				System.out.println(resultDate);
				
				for (int k = 0; k < treeNode.get("data").size(); k++) {
					String resultCandle = String.valueOf(treeNode.get("data").get(k).get(0));
					
//					System.out.println(resultCandle);
					 if(Double.parseDouble(resultDate)<Double.parseDouble(resultCandle)) {
						 
						 if(k==0) {
							 candleNum=k;
						 }else {
							 candleNum = k-1;
						 }
						 
						 break;
					 }
					 
					 
				}
				
				
				String cp = String.valueOf(treeNode.get("data").get(candleNum).get(2));
				String replace_cp = cp.replaceAll("\"", "");
				
				reportRepository.updateBankStateMentPrice(findByBankStateMent.get(j).getOrderId(), replace_cp);
				
//				System.out.println("orderId : " + findByBankStateMent.get(j).getOrderId());
//				System.out.println("종가 : "+replace_cp);
			}
			
		}
		
		
	}
	
	
}
