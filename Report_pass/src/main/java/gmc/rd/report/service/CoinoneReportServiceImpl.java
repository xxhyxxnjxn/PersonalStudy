package gmc.rd.report.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneCoinTransactionsDataVo;
import gmc.rd.report.api.coinone.vo.CoinoneTransactionVo;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.CoinoneState;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CoinoneStateRepository;
import gmc.rd.report.repository.ReportRepository3;

@Service
public class CoinoneReportServiceImpl implements CoinoneReportService{

	@Autowired
	private ApiRepository apiRepository;
	
	@Autowired
	private CoinoneApiService coinoneApiService;

	@Autowired
	private ApiRoadingStateRepository apiRoadingRepository;
	@Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;
	
	// 코인원
	@Autowired
	private ReportRepository3 reportRepository3;

	@Autowired
	private CoinoneStateRepository coinoneStateRepository;
	
	@Autowired
	private BithumbApiService bithumbApiService;
	
	
	@Transactional
	@Override
	public void selectReport(User user, ApiDto apiDto, String currency) throws Exception {

		Gson gson = new Gson();

		List<CoinoneTransactionVo> coinoneTransaction = null;
		//List<CoinoneCoinTransactionsDataVo> coinoneCoinTransactionsDataVo = null;

		List<ReportDto3> reportDto3 = null;

		List<Report3> report3 = null;

		String jsonStr = null;

				

				if (apiDto == null) {
					System.out.println("코인원 api 키 없음");
				} else {
					
					//int cnt = 0;
					
					//for (VmDto result : vmDto) {
						

						  //if db table이 true이면 인증키가 끼어듦 = true 평상시 false
		                  CoinoneState cs =  coinoneStateRepository.findOne();

		                  
		                  if(cs.isState()) {
		                     //sleep, 그다음
		                     Thread.sleep(2000);   //2초  delay
		                     System.out.println("api key 인증요청 deley----------------------------------------------------------------------------------");
		                     //db update 
		                     cs.setState(false);

		                     System.out.println("확인 3");
		                     System.out.println(cs.isState());
		                     
		                  }
						
//						if ((cnt % 5 == 0) && (cnt != 0)) {
//							System.out.println("delay되라 " + cnt);
//
//							Thread.sleep(2000);
//
//						}

						System.out.println("coin : " + currency);
						Date timestamp = new Date();
						System.out.println(timestamp);
						//System.out.println(cnt);
						
						
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("apiKey", apiDto.getApiKey());
						hashMap.put("secretKey", apiDto.getSecretKey());
						//cnt++;

						hashMap.put("currency",currency );
						
						coinoneTransaction = coinoneApiService.MyCompletedOrders2(hashMap); // 거래 내역
						//coinoneCoinTransactionsDataVo = coinoneApiService.coinTransacionHistory(hashMap); // 입출금 내역
						//Thread.sleep(2000);
						DecimalFormat df=new DecimalFormat("0.####");

						String before_orderId = "beforeOrderId";
						String orderId = null;
						int num=0;
						
						if (coinoneTransaction != null) {
						
							for(int i = 0 ; i<coinoneTransaction.size();i++) {
		                        
		                      
								coinoneTransaction.get(i).setTimestamp(coinoneTransaction.get(i).getTimestamp()+"000");
								coinoneTransaction.get(i).setQty(df.format( Double.valueOf(coinoneTransaction.get(i).getQty())));
								
								orderId = coinoneTransaction.get(i).getTimestamp()+currency+coinoneTransaction.get(i).getType()+coinoneTransaction.get(i).getQty().replace(".", "");
								
								if(before_orderId.equals(orderId)) {
									num++;
								}else {
									num=0;
								}
								
								//coinoneTransaction.get(i).setOrderId(coinoneTransaction.get(i).getTimestamp()+result.getVmId()+coinoneTransaction.get(i).getType()+coinoneTransaction.get(i).getQty().replace(".", ""));
								coinoneTransaction.get(i).setOrderId(orderId+num);
								
								before_orderId = orderId;
								   
		                     
		                     }
							
							
							jsonStr = new ObjectMapper().writeValueAsString(coinoneTransaction);
							Type listType = new TypeToken<List<ReportDto3>>() {
							}.getType();
							reportDto3 = gson.fromJson(jsonStr, listType);

							report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);
							
							for(int i=0; i<report3.size();i++) {
								report3.get(i).setUser(user);
								report3.get(i).setSite("coinone");
								report3.get(i).setCurrency(currency);
								if (report3.get(i).getType().equals("ask")) {
									report3.get(i).setType("매도");

								} else if (report3.get(i).getType().equals("bid")) {
									report3.get(i).setType("매수");
									BigDecimal bid_fee = new BigDecimal(report3.get(i).getFee());
									BigDecimal price = new BigDecimal(report3.get(i).getPrice());
									bid_fee = price.multiply(bid_fee);
									report3.get(i).setFee(bid_fee.toPlainString());

								}
								
							}
							reportRepository3.saveAll(report3);
						}
						
//						num=0;
//						if(coinoneCoinTransactionsDataVo!=null) {
//							for(int i = 0 ; i<coinoneCoinTransactionsDataVo.size();i++) {
//		                        
//						
//								coinoneCoinTransactionsDataVo.get(i).setTimestamp(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+"000");
//								coinoneCoinTransactionsDataVo.get(i).setQuantity(df.format(Double.valueOf(coinoneCoinTransactionsDataVo.get(i).getQuantity())));
//								
//								orderId = coinoneCoinTransactionsDataVo.get(i).getTimestamp()+result.getVmId()+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", "");
//								
//								if(before_orderId.equals(orderId)) {
//									num++;
//								}else {
//									num=0;
//								}
//								
//								//coinoneCoinTransactionsDataVo.get(i).setTxid(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+result.getVmId()+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", ""));
//								coinoneCoinTransactionsDataVo.get(i).setTxid(orderId+num);
//								
//								before_orderId = orderId;
//
//		                     }
//							
//							
//							jsonStr = new ObjectMapper().writeValueAsString(coinoneCoinTransactionsDataVo);
//							Type listType = new TypeToken<List<ReportDto3>>() {
//							}.getType();
//							reportDto3 = gson.fromJson(jsonStr, listType);
//							
//							report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);
//							
//							for(int i=0; i<report3.size();i++) {
//								report3.get(i).setUser(user);
//								report3.get(i).setSite("coinone");
//								report3.get(i).setCurrency(result.getVmId());
//								report3.get(i).setFee("0"+result.getVmId());
//								report3.get(i).setPrice("0.0");
//								if (report3.get(i).getType().equals("receive")) {
//									report3.get(i).setType("입금");
//
//								} else if (report3.get(i).getType().equals("send")) {
//									report3.get(i).setType("출금");
//
//								}
//							}
//							
//							reportRepository3.saveAll(report3);
//							
//						}
						

						
					//}
					
				}

			
		//return null;
		
	}

	@Transactional
	@Override
	public void selectBankStateMent(User user, ApiDto apiDto, String currency) throws Exception {

		Gson gson = new Gson();

		List<CoinoneCoinTransactionsDataVo> coinoneCoinTransactionsDataVo = null;

		List<ReportDto3> reportDto3 = null;

		List<Report3> report3 = null;

		String jsonStr = null;


//				Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
//				ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

				if (apiDto == null) {
					System.out.println("코인원 api 키 없음");
				} else {
					//int cnt = 0;
					
					//for (VmDto result : vmDto) {
						 
						  
						 // System.out.println(user.getMemId() + " "+apiDto.getSite());
						  //if db table이 true이면 인증키가 끼어듦 = true 평상시 false
		                  CoinoneState cs =  coinoneStateRepository.findOne();

		                  
		                  if(cs.isState()) {
		                     //sleep, 그다음
		                     Thread.sleep(2000);   //2초  delay
		                     System.out.println("api key 인증요청 deley----------------------------------------------------------------------------------");
		                     //db update 
		                     cs.setState(false);

		                     System.out.println("확인 3");
		                     System.out.println(cs.isState());
		                     
		                  }
						
//						if ((cnt % 5 == 0) && (cnt != 0)) {
//							System.out.println("delay되라 " + cnt);
//
//							Thread.sleep(2000);
//
//						}

						System.out.println("coin : " + currency);
						Date timestamp = new Date();
						System.out.println(timestamp);
						//System.out.println(cnt);

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("apiKey", apiDto.getApiKey());
						hashMap.put("secretKey", apiDto.getSecretKey());
						//cnt++;

						hashMap.put("currency",currency );
						
						coinoneCoinTransactionsDataVo = coinoneApiService.coinTransacionHistory(hashMap); // 입출금 내역
						//Thread.sleep(2000);
						DecimalFormat df=new DecimalFormat("0.####");

						String before_orderId = "beforeOrderId";
						String orderId = null;
						int num=0;

						if(coinoneCoinTransactionsDataVo!=null) {
							for(int i = 0 ; i<coinoneCoinTransactionsDataVo.size();i++) {
		                        
						
								coinoneCoinTransactionsDataVo.get(i).setTimestamp(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+"000");
								coinoneCoinTransactionsDataVo.get(i).setQuantity(df.format(Double.valueOf(coinoneCoinTransactionsDataVo.get(i).getQuantity())));
								
								orderId = coinoneCoinTransactionsDataVo.get(i).getTimestamp()+currency+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", "");
								
								if(before_orderId.equals(orderId)) {
									num++;
								}else {
									num=0;
								}
								
								//coinoneCoinTransactionsDataVo.get(i).setTxid(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+result.getVmId()+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", ""));
								coinoneCoinTransactionsDataVo.get(i).setTxid(orderId+num);
								
								before_orderId = orderId;

		                     }
							
							
							jsonStr = new ObjectMapper().writeValueAsString(coinoneCoinTransactionsDataVo);
							Type listType = new TypeToken<List<ReportDto3>>() {
							}.getType();
							reportDto3 = gson.fromJson(jsonStr, listType);
							
							report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);
							
							for(int i=0; i<report3.size();i++) {
								report3.get(i).setUser(user);
								report3.get(i).setSite("coinone");
								report3.get(i).setCurrency(currency);
								report3.get(i).setFee("0"+currency);
								report3.get(i).setPrice("0.0");
								if (report3.get(i).getType().equals("receive")) {
									report3.get(i).setType("입금");

								} else if (report3.get(i).getType().equals("send")) {
									report3.get(i).setType("출금");

								}
							}
							
							reportRepository3.saveAll(report3);
							
						}
						


					}
				//}

			
		//return null;
		
	}
	
	@Transactional
	@Override
	public void selectReportUpdate(User user, ApiDto apiDto, String currency) throws Exception {

		Gson gson = new Gson();

		List<CoinoneTransactionVo> coinoneTransaction = null;
		//List<CoinoneCoinTransactionsDataVo> coinoneCoinTransactionsDataVo = null;

		List<ReportDto3> reportDto3_transaction = null;

		List<Report3> report3_transaction = null;
		
		Report3 findByOrderId = null;

		String jsonStr = null;


//				Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
//				ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

				if (apiDto == null) {
					System.out.println("코인원 api 키 없음");
				} else {
					//int cnt = 0;
				//	for (VmDto result : vmDto) {
						  //if db table이 true이면 인증키가 끼어듦 = true 평상시 false
		                  CoinoneState cs =  coinoneStateRepository.findOne();

		                  
		                  if(cs.isState()) {
		                     //sleep, 그다음
		                     Thread.sleep(2000);   //2초  delay
		                     System.out.println("api key 인증요청 deley----------------------------------------------------------------------------------");
		                     //db update 
		                     cs.setState(false);

		                     System.out.println("확인 3");
		                     System.out.println(cs.isState());
		                     
		                  }
						
//						if ((cnt % 5 == 0) && (cnt != 0)) {
//							System.out.println("delay되라 " + cnt);
//
//							Thread.sleep(2000);
//
//						}

						System.out.println("coin : " + currency);
						Date timestamp = new Date();
						System.out.println(timestamp);
						//System.out.println(cnt);

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("apiKey", apiDto.getApiKey());
						hashMap.put("secretKey", apiDto.getSecretKey());
						//cnt++;

						hashMap.put("currency",currency );
						
						coinoneTransaction = coinoneApiService.MyCompletedOrders2(hashMap); // 거래 내역
						//coinoneCoinTransactionsDataVo = coinoneApiService.coinTransacionHistory(hashMap); // 입출금 내역
						//Thread.sleep(2000);
						DecimalFormat df=new DecimalFormat("0.####");

						String before_orderId = "beforeOrderId";
						String orderId = null;
						int num=0;
						
						if (coinoneTransaction != null) {
						
							for(int i = 0 ; i<coinoneTransaction.size();i++) {
								coinoneTransaction.get(i).setTimestamp(coinoneTransaction.get(i).getTimestamp()+"000");
								coinoneTransaction.get(i).setQty(df.format( Double.valueOf(coinoneTransaction.get(i).getQty())));
		                        
								orderId = coinoneTransaction.get(i).getTimestamp()+currency+coinoneTransaction.get(i).getType()+coinoneTransaction.get(i).getQty().replace(".", "");
								
								if(before_orderId.equals(orderId)) {
									num++;
								}else {
									num=0;
								}
								
								//coinoneTransaction.get(i).setOrderId(coinoneTransaction.get(i).getTimestamp()+result.getVmId()+coinoneTransaction.get(i).getType()+coinoneTransaction.get(i).getQty().replace(".", ""));
								coinoneTransaction.get(i).setOrderId(orderId+num);
								
								before_orderId = orderId;
								
								findByOrderId = reportRepository3.findByOrderIdOne(currency,user.getMemId());
								
								if(findByOrderId != null && Long.parseLong(findByOrderId.getTransactionDate().toString())>=Long.parseLong(coinoneTransaction.get(i).getTimestamp())) {
									coinoneTransaction.remove(i);
									i--;
									System.out.println("중복 제거");
								}
								
		                     
		                     }
							
							
							jsonStr = new ObjectMapper().writeValueAsString(coinoneTransaction);
							Type listType = new TypeToken<List<ReportDto3>>() {
							}.getType();
							reportDto3_transaction = gson.fromJson(jsonStr, listType);

							report3_transaction = ReportDtoMapper3.INSTANCE.toEntity(reportDto3_transaction);
							
							for(int i=0; i<report3_transaction.size();i++) {
								report3_transaction.get(i).setUser(user);
								report3_transaction.get(i).setSite("coinone");
								report3_transaction.get(i).setCurrency(currency);
								if (report3_transaction.get(i).getType().equals("ask")) {
									report3_transaction.get(i).setType("매도");

								} else if (report3_transaction.get(i).getType().equals("bid")) {
									report3_transaction.get(i).setType("매수");
									BigDecimal bid_fee = new BigDecimal(report3_transaction.get(i).getFee());
									BigDecimal price = new BigDecimal(report3_transaction.get(i).getPrice());
									bid_fee = price.multiply(bid_fee);
									report3_transaction.get(i).setFee(bid_fee.toPlainString());

								}
								
							}
							
							
							reportRepository3.saveAll(report3_transaction);
						}

				}

		
	}

	@Transactional
	@Override
	public void selectBankStateMentUpdate(User user, ApiDto apiDto, String currency) throws Exception {

		Gson gson = new Gson();

		//List<CoinoneTransactionVo> coinoneTransaction = null;
		List<CoinoneCoinTransactionsDataVo> coinoneCoinTransactionsDataVo = null;

		List<ReportDto3> reportDto3_bank = null;

		List<Report3> report3_bank = null;
		
		Report3 findByOrderId = null;

		String jsonStr = null;


//				Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
//				ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

				if (apiDto == null) {
					System.out.println("코인원 api 키 없음");
				} else {
					//int cnt = 0;
				//	for (VmDto result : vmDto) {
						  //if db table이 true이면 인증키가 끼어듦 = true 평상시 false
		                  CoinoneState cs =  coinoneStateRepository.findOne();

		                  
		                  if(cs.isState()) {
		                     //sleep, 그다음
		                     Thread.sleep(2000);   //2초  delay
		                     System.out.println("api key 인증요청 deley----------------------------------------------------------------------------------");
		                     //db update 
		                     cs.setState(false);

		                     System.out.println("확인 3");
		                     System.out.println(cs.isState());
		                     
		                  }
						
//						if ((cnt % 5 == 0) && (cnt != 0)) {
//							System.out.println("delay되라 " + cnt);
//
//							Thread.sleep(2000);
//
//						}

						System.out.println("coin : " + currency);
						Date timestamp = new Date();
						System.out.println(timestamp);
						//System.out.println(cnt);

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("apiKey", apiDto.getApiKey());
						hashMap.put("secretKey", apiDto.getSecretKey());
						//cnt++;

						hashMap.put("currency",currency );
						
						//coinoneTransaction = coinoneApiService.MyCompletedOrders2(hashMap); // 거래 내역
						coinoneCoinTransactionsDataVo = coinoneApiService.coinTransacionHistory(hashMap); // 입출금 내역
						//Thread.sleep(2000);
						DecimalFormat df=new DecimalFormat("0.####");

						String before_orderId = "beforeOrderId";
						String orderId = null;
						int num=0;

						if(coinoneCoinTransactionsDataVo!=null) {
							for(int i = 0 ; i<coinoneCoinTransactionsDataVo.size();i++) {
								coinoneCoinTransactionsDataVo.get(i).setTimestamp(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+"000");
								coinoneCoinTransactionsDataVo.get(i).setQuantity(df.format(Double.valueOf(coinoneCoinTransactionsDataVo.get(i).getQuantity())));
								
								orderId = coinoneCoinTransactionsDataVo.get(i).getTimestamp()+currency+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", "");
								
								if(before_orderId.equals(orderId)) {
									num++;
								}else {
									num=0;
								}
								
								//coinoneCoinTransactionsDataVo.get(i).setTxid(coinoneCoinTransactionsDataVo.get(i).getTimestamp()+result.getVmId()+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getQuantity().replace(".", ""));
								coinoneCoinTransactionsDataVo.get(i).setTxid(orderId+num);
								
								before_orderId = orderId;
								
								findByOrderId = reportRepository3.findByOrderIdOne(currency,user.getMemId());
								
								if(findByOrderId != null && Long.parseLong(findByOrderId.getTransactionDate())>=Long.parseLong(coinoneCoinTransactionsDataVo.get(i).getTimestamp())) {
									coinoneCoinTransactionsDataVo.remove(i);
									i--;
								}

		                     }
							
							
							jsonStr = new ObjectMapper().writeValueAsString(coinoneCoinTransactionsDataVo);
							Type listType = new TypeToken<List<ReportDto3>>() {
							}.getType();
							reportDto3_bank = gson.fromJson(jsonStr, listType);
							
							report3_bank = ReportDtoMapper3.INSTANCE.toEntity(reportDto3_bank);
							
							for(int i=0; i<report3_bank.size();i++) {
								report3_bank.get(i).setUser(user);
								report3_bank.get(i).setSite("coinone");
								report3_bank.get(i).setCurrency(currency);
								report3_bank.get(i).setFee("0"+currency);
								report3_bank.get(i).setPrice("0.0");
								if (report3_bank.get(i).getType().equals("receive")) {
									report3_bank.get(i).setType("입금");

								} else if (report3_bank.get(i).getType().equals("send")) {
									report3_bank.get(i).setType("출금");

								}
							}
							
							reportRepository3.saveAll(report3_bank);
							
						}
						


				//	}
				}

			
		//return null;
		
	}
	
	@Transactional
	@Override
	public void selectCandleStcik(User user){
		List<Report3> groupByCurrencyBankStateMent = reportRepository3.groupByCurrencyBankStateMent(user.getMemId());
		String currency = null;
		try {
		
		for(int i=0;i<groupByCurrencyBankStateMent.size();i++) {
			String result = bithumbApiService.getCandleStick(groupByCurrencyBankStateMent.get(i).getCurrency(),"1h");
			
			currency = groupByCurrencyBankStateMent.get(i).getCurrency();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode treeNode = mapper.readTree(result);
			
			List<Report3> findByBankStateMent = reportRepository3.findByBankStateMent(user.getMemId(), groupByCurrencyBankStateMent.get(i).getCurrency());
			
			
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
				
				reportRepository3.updateBankStateMentPrice(findByBankStateMent.get(j).getOrderId(), replace_cp);

			}
			
		}
		}catch(Exception e) {
			reportRepository3.updateBankStateMentPrice(currency, user.getMemId());
		}
	}
	
	@Transactional
	@Override
	public void selectCandleStcikUpdate(User user) throws Exception{
		List<Report3> groupByCurrencyBankStateMent = reportRepository3.groupByCurrencyBankStateMentUpdate(user.getMemId());

		
		for(int i=0;i<groupByCurrencyBankStateMent.size();i++) {
			String result = bithumbApiService.getCandleStick(groupByCurrencyBankStateMent.get(i).getCurrency(),"1h");
			
			if(!result.contains("5500")) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode treeNode = mapper.readTree(result);
				
				List<Report3> findByBankStateMent = reportRepository3.findByBankStateMentUpdate(user.getMemId(), groupByCurrencyBankStateMent.get(i).getCurrency());
				
				
				int candleNum = 0;
				
				for (int j = 0; j < findByBankStateMent.size(); j++) {
					String str = findByBankStateMent.get(j).getTransactionDate();
					String resultDate = str.substring(0, str.length()-3);
					
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
					
					reportRepository3.updateBankStateMentPrice(findByBankStateMent.get(j).getOrderId(), replace_cp);

				}
			}else {
				reportRepository3.updateBankStateMentPrice(groupByCurrencyBankStateMent.get(i).getCurrency(), user.getMemId());
			}

		}
	}
}
