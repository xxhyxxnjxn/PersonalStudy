package gmc.rd.report.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;
import gmc.rd.report.api.upbit.vo.Withdraws;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.UserRepository;

@Service
public class UpbitReportServiceUpdate implements UpbitReportService{
	// 업비트
		@Autowired
		private ReportRepository2 reportRepository2;

		@Autowired
		private UpbitApiService upbitApiService;

		@Autowired
		private ApiRepository apiRepository;

		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private BithumbApiService bithumbApiService;

		
		@Override
		public List<MarketAll> getCoinList() throws Exception {
			// HashMap<String, String> marketHash = new HashMap<String, String>();
			upbitApiService.getMarketAll();
			System.out.println("marketAll 불러오기 끝");
			return null;
		}

		@Override
		public List<Deposit> getDeposit(String userId, String currencyselect,User user,Api api) throws Exception {
			List<UpbitTransactionVo> upbitTransaction = null;
			List<Deposit> deposit = null;

			List<ReportDto2> reportDto2 = null;

			List<Report2> report2 = null;
			
			Report2 findByOrderId = null;
			
			Gson gson = new Gson();
			String jsonStr = null;
			//User user = userRepository.findByMemId1(userId);
			//Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");


			ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);


			if (apiDto == null) {
				System.out.println("업비트 api 키 없음");
			} else {
				int page_cnt = 1;
					//while(true) {
						
					// System.out.println("currencyselect : " + currencyselect);
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("apiKey", api.getApiKey());
					hash.put("secretKey", api.getSecretKey());
					hash.put("currency", currencyselect);
					hash.put("page", String.valueOf(page_cnt));
					String deposit1 = upbitApiService.getDeposits(hash);
					if (deposit1.equals("[]")) {
						return null;
					} else {
						System.out.println("deposits update값" + deposit1);
						Type listType = new TypeToken<List<Deposit>>() {
						}.getType();
						deposit = gson.fromJson(deposit1, listType);
					}
					

					if (deposit == null) {
//						System.out.println("ddd");
						//break;
						
					} else {

						jsonStr = new ObjectMapper().writeValueAsString(deposit);
						Type listType = new TypeToken<List<ReportDto2>>() {
						}.getType();
						reportDto2 = gson.fromJson(jsonStr, listType);

						report2 = ReportDtoMapper2.INSTANCE.toEntity(reportDto2);
						
						for (int j = 0; j < report2.size(); j++) {
							
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
							Date date = format.parse(report2.get(j).getTransactionDate());
							long timestamp = date.getTime();
							
							findByOrderId = reportRepository2.findByOrderIdOneDeposite(currencyselect, user.getMemId());
							
							//if(findByOrderId != null) {
							if(findByOrderId != null && Long.parseLong(findByOrderId.getTransactionDate())>=timestamp) {
								report2.remove(j);
								j--;
							}else {
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.MONTH,-6);
								
//								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//								Date date = format.parse(report2.get(j).getTransactionDate());
								Calendar cal2 = Calendar.getInstance();
								cal2.setTime(date);
								
								int compare = cal.compareTo(cal2);
//								
								if(compare>0) {
									report2.remove(j);
									j--;
									
								}else if(compare < 0 ) {
									if (report2.get(j).getTransactionDate() == null) {
										report2.remove(j);
										j--;
									} else {
										report2.get(j).setUser(user);
										report2.get(j).setSite("upbit");

										String currency = report2.get(j).getCurrency();


										report2.get(j).setCurrency(currency); // 코인 수정 KRW-BTC -> BTC

//										long timestamp = date.getTime();
										report2.get(j).setTransactionDate(String.valueOf(timestamp)); // 업비트에서 제공해주는 시간을 unix시간으로
																										// 바꾸는 작업
										report2.get(j).setPrice("0.0");
										if (report2.get(j).getType().equals("deposit")) {
											report2.get(j).setType("입금");
										}
									}
									BigDecimal units = new BigDecimal(report2.get(j).getUnits());
									BigDecimal fee = new BigDecimal(report2.get(j).getFee());
									units = units.add(fee);
									report2.get(j).setUnits(units.toPlainString());
									
									
								}
							}
							
							
						}
						reportRepository2.saveAll(report2);
						//page_cnt++;
					}
					
					//}
			}
			return deposit;
		}
		
		
		@Override
		public List<Withdraws> getWithdraw(String userId, String currencyselect,User user,Api api) throws Exception {
			List<UpbitTransactionVo> upbitTransaction = null;
			List<Withdraws> withdraw = null;

			List<ReportDto2> reportDto2 = null;

			List<Report2> report2 = null;
			
			Report2 findByOrderId = null;
			Gson gson = new Gson();
			String jsonStr = null;
//			User user = userRepository.findByMemId1(userId);
//			Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");


			ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);


			if (apiDto == null) {
				System.out.println("업비트 api 키 없음");
			} else {
				int page_cnt = 1;
					//while(true) {
						
					// System.out.println("currencyselect : " + currencyselect);
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put("apiKey", api.getApiKey());
					hash.put("secretKey", api.getSecretKey());
					hash.put("currency", currencyselect);
					hash.put("page", String.valueOf(page_cnt));
					String withdraw1 = upbitApiService.getWithdraws(hash);
					if (withdraw1.equals("[]")) {
						return null;
					} else {
						System.out.println("withdraw1 update 값" + withdraw1);
						Type listType = new TypeToken<List<Deposit>>() {
						}.getType();
						withdraw = gson.fromJson(withdraw1, listType);
					}
					
					if (withdraw == null) {
						//break;
						
					} else {

						jsonStr = new ObjectMapper().writeValueAsString(withdraw);
						Type listType = new TypeToken<List<ReportDto2>>() {
						}.getType();
						reportDto2 = gson.fromJson(jsonStr, listType);

						report2 = ReportDtoMapper2.INSTANCE.toEntity(reportDto2);

						for (int j = 0; j < report2.size(); j++) {
							
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
							Date date = format.parse(report2.get(j).getTransactionDate());
							long timestamp = date.getTime();
							
							findByOrderId = reportRepository2.findByOrderIdOneWithdraw(currencyselect, user.getMemId());
							
							//if(findByOrderId != null) {
							if(findByOrderId != null && Long.parseLong(findByOrderId.getTransactionDate())>=timestamp) {
								report2.remove(j);
								j--;
							}else {
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.MONTH,-6);
								
//								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//								Date date = format.parse(report2.get(j).getTransactionDate());
								Calendar cal2 = Calendar.getInstance();
								cal2.setTime(date);
								
								int compare = cal.compareTo(cal2);
								
								if(compare>0) {
									report2.remove(j);
									j--;
									
								}else if(compare < 0 ) {
									if (report2.get(j).getTransactionDate() == null) {
										report2.remove(j);
										j--;
									} else {
										report2.get(j).setUser(user);
										report2.get(j).setSite("upbit");

										String currency = report2.get(j).getCurrency();

										report2.get(j).setCurrency(currency); // 코인 수정 KRW-BTC -> BTC

//										SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//										Date date = format.parse(report2.get(j).getTransactionDate());
//										long timestamp = date.getTime();
										report2.get(j).setTransactionDate(String.valueOf(timestamp)); // 업비트에서 제공해주는 시간을 unix시간으로
																										// 바꾸는 작업
										report2.get(j).setPrice("0.0");
										if (report2.get(j).getType().equals("withdraw")) {
											report2.get(j).setType("출금");
										}
									}
									BigDecimal units = new BigDecimal(report2.get(j).getUnits());
									BigDecimal fee = new BigDecimal(report2.get(j).getFee());
									units = units.add(fee);
									report2.get(j).setUnits(units.toPlainString());
									
								}
							}
							
							
						}
						reportRepository2.saveAll(report2);
						//page_cnt++;
					}
					
					//}
			}
			return withdraw;
		}

		
		
		@Transactional
		@Override
		public List<Report2> selectReport(String sitename, int page_cnt, User user) throws Exception {

			Gson gson = new Gson();

			List<UpbitTransactionVo> upbitTransaction = null;
			List<UpbitTransactionVo> upbitTransaction2 = null;

			List<ReportDto2> reportDto2 = null;

			List<Report2> report2 = null;

			String jsonStr = null;

			List<User> userList = userRepository.findAll();

			List<Trades> tradeslist = new ArrayList<>();

			Report2 findByOrderId = null;
			// 본데 page_cnt 가 있던자리
			// int page_cnt =1;


			Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");

			ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
			if (apiDto == null) {
				System.out.println("업비트 api 키 없음");
			} else {
				// for for(;;) {

				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("apiKey", api.getApiKey());
				hash.put("secretKey", api.getSecretKey());
				hash.put("state", "done");
				hash.put("page", String.valueOf(page_cnt));
				upbitTransaction = upbitApiService.getOrderList(hash);

				
				if (upbitTransaction.isEmpty()) {
					System.out.println("페이지 끝");
					// break;
					return null;
				} else {

					for (int i = 0; i < upbitTransaction.size(); i++) {

						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MONTH,-6);

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
						Date date = format.parse(upbitTransaction.get(i).getCreated_at());
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(date);
						
						int compare = cal.compareTo(cal2);
						
						if(compare>0) {
							return null;
							
						}
						

					}
					
					for (int i = 0; i < upbitTransaction.size(); i++) {

//						Calendar cal = Calendar.getInstance();
//						cal.add(Calendar.MONTH,-6);
//						
//						cal.set(2019, Calendar.DECEMBER , 31,0,0,0);
//						cal.set(Calendar.MILLISECOND, 0);
//						
//						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
//						Date date = format.parse(upbitTransaction.get(i).getCreated_at());
//						Calendar cal2 = Calendar.getInstance();
//						cal2.setTime(date);
//						
//						int compare = cal.compareTo(cal2);
//						
//						if(compare>0) {
//							upbitTransaction.remove(i);
//							i--;
//							//System.out.println(upbitTransaction.get(i).getCreated_at());
//							
//						}else if(compare < 0 ) {
							if (upbitTransaction.get(i).getMarket().contains("BTC-")) {
								// System.out.println("삭제할 UUID : " + upbitTransaction2.get(i).getUuid());
								upbitTransaction.remove(i);
								// System.out.println("다음 UUID : " + upbitTransaction2.get(i).getUuid());
								i--;
							}
						//}
						

					}
					
					


					System.out.println("orderDetail 시작!");
					for (int i = 0; i < upbitTransaction.size(); i++) {

						try {
							hash.put("uuid", upbitTransaction.get(i).getUuid());
							//
							
							List<Trades> trades = upbitApiService.getOrderDetail2(hash);

							//
							for (int j = 0; j < trades.size(); j++) {
								
								trades.get(j).setCurrency(upbitTransaction.get(i).getMarket());
								trades.get(j).setFee(upbitTransaction.get(i).getPaid_fee());
								tradeslist.add(trades.get(j));



							}
//		                              System.out.println("trades에서 가져온 price : " + trades.get(0).getPrice());
							//
//		                              upbitTransaction.get(i).setPrice(trades.get(0).getPrice());
//		                              upbitTransaction.get(i).setCreated_at(trades.get(0).getCreated_at());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
//		                        }
					}
					System.out.println("orderDetail 끝!");

				}



				jsonStr = new ObjectMapper().writeValueAsString(tradeslist);
				Type listType = new TypeToken<List<ReportDto2>>() {
				}.getType();
				reportDto2 = gson.fromJson(jsonStr, listType);

				report2 = ReportDtoMapper2.INSTANCE.toEntity(reportDto2);

				for (int j = 0; j < report2.size(); j++) {

					String currency = report2.get(j).getCurrency();
					currency = currency.replace("KRW-", "");
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
					Date date = format.parse(report2.get(j).getTransactionDate());
					long timestamp = date.getTime();

					findByOrderId = reportRepository2.findByOrderIdOne(currency, user.getMemId());
					
					//if(findByOrderId != null) {
					if(findByOrderId!=null && Long.parseLong(findByOrderId.getTransactionDate())>=timestamp) {
						report2.remove(j);
						j--;
					}else{
						report2.get(j).setUser(user);
						report2.get(j).setSite(sitename);
//						String currency = report2.get(j).getCurrency();
//						currency = currency.replace("KRW-", "");
						report2.get(j).setCurrency(currency); // 코인 수정 KRW-BTC -> BTC

//						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");	
//						Date date = format.parse(report2.get(j).getTransactionDate());
//						long timestamp = date.getTime();
						report2.get(j).setTransactionDate(String.valueOf(timestamp)); // 업비트에서 제공해주는 시간을 unix시간으로 바꾸는 작업

						if (report2.get(j).getType().equals("bid")) {
							report2.get(j).setType("매수");
						} else if (report2.get(j).getType().equals("ask")) {
							report2.get(j).setType("매도");
						}
						BigDecimal units = new BigDecimal(report2.get(j).getUnits());
						BigDecimal price = new BigDecimal(report2.get(j).getPrice());
						BigDecimal total_price = units.multiply(price);
						BigDecimal fee = new BigDecimal("0.0005");
						BigDecimal fee_cal = total_price.multiply(fee).setScale(2, BigDecimal.ROUND_FLOOR);
						report2.get(j).setFee(fee_cal.toPlainString());
					}

				//	}
					
				}
				reportRepository2.saveAll(report2);
				
//		                     setAvgPrice_upbit(sitename, report2, user);
			}

			// }
			return report2;
		}
		
		@Transactional
		@Override
		public List<Report2> selectReport2(String sitename, int page_cnt, User user) throws Exception{
			
			return null;
		}
		
		@Transactional
		@Override
		public void selectCandleStick(User user){
			List<Report2> groupByCurrencyBankStateMent = reportRepository2.groupByCurrencyBankStateMentUpdate(user.getMemId());
			String currency = null;
			try {
				for(int i=0;i<groupByCurrencyBankStateMent.size();i++) {
					String result = bithumbApiService.getCandleStick(groupByCurrencyBankStateMent.get(i).getCurrency(),"6h");
					
					currency = groupByCurrencyBankStateMent.get(i).getCurrency();
					ObjectMapper mapper = new ObjectMapper();
					JsonNode treeNode = mapper.readTree(result);
					
					List<Report2> findByBankStateMent = reportRepository2.findByBankStateMentUpdate(user.getMemId(), groupByCurrencyBankStateMent.get(i).getCurrency());
					
					int candleNum = 0;
					for (int j = 0; j < findByBankStateMent.size(); j++) {
						String resultDate = findByBankStateMent.get(j).getTransactionDate();
						
						for (int k = 0; k < treeNode.get("data").size(); k++) {
							String resultCandle = String.valueOf(treeNode.get("data").get(k).get(0));

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
						
						reportRepository2.updateBankStateMentPrice(findByBankStateMent.get(j).getOrderId(), replace_cp);

					}
					
				}
			}catch(Exception e) {
				reportRepository2.updateBankStateMentPrice(currency, user.getMemId());
			}

		}
		
		
}
