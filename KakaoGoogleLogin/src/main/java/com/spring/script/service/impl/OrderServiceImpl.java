package com.spring.script.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.spring.api.bithumb.service.ApiService;
import com.spring.api.upbit.ApiUpbit;
import com.spring.coinoneh.api.service.CoinoneApiService;
import com.spring.script.dao.ScriptDao;
import com.spring.script.service.OrderService;
import com.spring.script.service.ScriptService;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;
import com.spring.script.vo.SysOrderlistVo;

@EnableAsync
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	ApiService bithumbApiService;

	@Autowired
	ApiUpbit upbitApiService;

	@Autowired
	CoinoneApiService coinoneApiSerivce;

	@Autowired
	ScriptService scriptService;

	@Autowired
	ScriptDao scriptDao;

	String set_str = "0.01";
	double set_dou = 0.01;

	@Override
	@Async
	public void bithumbOrder(List<SysBotCkVo> sysBotList_bithumb, String signal_side, HashMap<String, String> hashmap)
			throws ParseException {
		// System.out.println(hash);
		for (int i = 0; i < sysBotList_bithumb.size(); i++) {
			String m_id = "";
			if (!sysBotList_bithumb.isEmpty()) {
				String currency = "";
				List<ApiVo> apiList = new ArrayList<>();

				HashMap<String, String> hash = new HashMap<String, String>(); // Api_tbl에서 apiKey, secretKey, currency
																				// 담는
																				// hashmap

				HashMap<String, String> apiHash = new HashMap<String, String>();
				m_id = sysBotList_bithumb.get(i).getM_id();
				apiHash.put("m_id", sysBotList_bithumb.get(i).getM_id());
				hash.put("m_id", sysBotList_bithumb.get(i).getM_id());
				apiHash.put("site", sysBotList_bithumb.get(i).getSite());
				hash.put("site", sysBotList_bithumb.get(i).getSite());
				hashmap.put("site", sysBotList_bithumb.get(i).getSite());
				hashmap.put("bot_name", sysBotList_bithumb.get(i).getBot_name());
				
				
				System.out.println("빗썸 해시ㅂ맵"+hashmap);
				System.out.println("빗썸 사이트"+sysBotList_bithumb.get(i).getSite());
				
				
				apiList = scriptService.getApi(apiHash);
				currency = sysBotList_bithumb.get(i).getCurrency();
				

				//세팅값 , 봇이름
				String set_dou =sysBotList_bithumb.get(i).getSet_per();
				String bot_name = sysBotList_bithumb.get(i).getBot_name();
				
				System.out.println("봇네임"+bot_name);
				

				for (int j = 0; j < apiList.size(); j++) {
					String site = apiList.get(j).getSite();
					System.out.println("에이피아이"+site);
					hash.put("apiKey", apiList.get(j).getApiKey());
					hash.put("secretKey", apiList.get(j).getSecretKey());
					hash.put("currency", currency);
				}

				JSONParser parser = new JSONParser();
				Object obj;
				JSONObject jsonObject;

				String balance_result = bithumbApiService.getBalance(hash);
				String orderbook_result = bithumbApiService.getOrderBook(hash);

				obj = parser.parse(balance_result);
				jsonObject = (JSONObject) obj;
				JSONObject balance_data = (JSONObject) jsonObject.get("data");
				String available_krw = (String) balance_data.get("available_krw");
				String available_coin = (String) balance_data.get("available_" + hash.get("currency").toLowerCase());

				obj = parser.parse(orderbook_result);
				jsonObject = (JSONObject) obj;
				JSONObject orderbook_data = (JSONObject) jsonObject.get("data");
				JSONArray asks = (JSONArray) orderbook_data.get("asks");
				JSONObject ask = (JSONObject) asks.get(10);
				String ask_price = (String) ask.get("price"); // 매도가의 10번째 가격
			
				if (signal_side.equals("Buy")) {
					BigDecimal setting = new BigDecimal(set_dou);
					BigDecimal c_krw = new BigDecimal(available_krw);
					BigDecimal c_units = c_krw.multiply(setting);
					BigDecimal c_ask_price = new BigDecimal(ask_price);
					BigDecimal units = c_units.divide(c_ask_price, 4, RoundingMode.FLOOR);
					hash.put("units", units.toString());
					hash.put("price", ask_price);

					String bithumbresult = bithumbApiService.limitBuyOrder(hash);
					System.out.println(bithumbresult);
					obj = parser.parse(bithumbresult);
					jsonObject = (JSONObject) obj;
					if (jsonObject.get("status").equals("0000")) { //결제 성공시
						hash.put("orderId", jsonObject.get("order_id").toString());
						System.out.println(bithumbApiService.getOrderDetail(hash));
						obj = parser.parse(bithumbApiService.getOrderDetail(hash));
						jsonObject = (JSONObject) obj;
						JSONObject data = (JSONObject) jsonObject.get("data");
						JSONArray contract = (JSONArray) data.get("contract");

						BigDecimal sum_price = new BigDecimal("0");
						BigDecimal sum_units = new BigDecimal("0");
						BigDecimal sum_fee = new BigDecimal("0");

						if (contract.size() != 1) {
							for (i = 0; i < contract.size(); i++) {
								JSONObject contractJson = (JSONObject) contract.get(i);
								BigDecimal price = new BigDecimal(contractJson.get("price").toString());
								BigDecimal add_units = new BigDecimal(contractJson.get("units").toString());
								BigDecimal add_fee = new BigDecimal(contractJson.get("fee").toString());
							
								price = price.multiply(add_units);
								
								sum_price = sum_price.add(price);
								sum_units = sum_units.add(add_units);
								sum_fee = sum_fee.add(add_fee);
							}

							hash.put("tot_price", sum_price.toString());
							sum_price = sum_price.divide(sum_units,4, RoundingMode.FLOOR);

							hash.put("price", sum_price.toString());
							hash.put("units", sum_units.toString());
							hash.put("fee", sum_fee.toString());
							hash.put("side", "bid");
							hash.put("revenue", "0");
							hash.put("bot_name", bot_name);
							hashmap.put("bot_name", bot_name);
							
							

						} else {
							JSONObject contractJson = (JSONObject) contract.get(0);

							hash.put("price", contractJson.get("price").toString()); 
							hash.put("tot_price", contractJson.get("total").toString()); 
							hash.put("units", contractJson.get("units").toString());
							hash.put("fee", contractJson.get("fee").toString());
							hash.put("side", "bid");
							hash.put("revenue", "0");
							hash.put("bot_name", bot_name);
							hashmap.put("bot_name", bot_name);
							
								

						}
						hashmap.put("position", "Buy");
						scriptDao.bithumb_orderlist_insert(hash);
						scriptService.updateSysBotCk_buy(hashmap);
						System.out.println("빗썸 매수 해시맵"+hashmap);
					}

					System.out.println(m_id + "빗썸 매수");

				} else {



					SysOrderlistVo sysorderlistvo = scriptDao.bithumb_orderlist_select(hash);
					
					if(Float.parseFloat(sysorderlistvo.getUnits())<=Float.parseFloat(available_coin)) {
						hash.put("units", sysorderlistvo.getUnits());
						String bithumbresult =bithumbApiService.marketSellOrder(hash);
						System.out.println(bithumbresult);
						obj = parser.parse(bithumbresult);
						jsonObject = (JSONObject) obj;
						if (jsonObject.get("status").equals("0000")) { //결제 성공시
							hash.put("orderId", jsonObject.get("order_id").toString());
							System.out.println(bithumbApiService.getOrderDetail(hash));
							obj = parser.parse(bithumbApiService.getOrderDetail(hash));
							jsonObject = (JSONObject) obj;
							JSONObject data = (JSONObject) jsonObject.get("data");
							JSONArray contract = (JSONArray) data.get("contract");

							BigDecimal sum_price = new BigDecimal("0");
							BigDecimal sum_units = new BigDecimal("0");
							BigDecimal sum_fee = new BigDecimal("0");

							if (contract.size() != 1) {
								for (i = 0; i < contract.size(); i++) {
									JSONObject contractJson = (JSONObject) contract.get(i);
									BigDecimal price = new BigDecimal(contractJson.get("price").toString());
									BigDecimal add_units = new BigDecimal(contractJson.get("units").toString());
									BigDecimal add_fee = new BigDecimal(contractJson.get("fee").toString());
								
									price = price.multiply(add_units);
									
									sum_price = sum_price.add(price);
									sum_units = sum_units.add(add_units);
									sum_fee = sum_fee.add(add_fee);
								}

								hash.put("tot_price", sum_price.toString());
								sum_price = sum_price.divide(sum_units,4, RoundingMode.FLOOR);

								hash.put("price", sum_price.toString());
								hash.put("units", sum_units.toString());
								hash.put("fee", sum_fee.toString());
								hash.put("side", "ask");
								BigDecimal getTotPrice = new BigDecimal(sysorderlistvo.getTot_price());
								BigDecimal revenue = new BigDecimal(sum_price.toString());
								revenue = revenue.subtract(getTotPrice);
								revenue = revenue.divide(getTotPrice,2, RoundingMode.FLOOR);
								
								hash.put("revenue", revenue.toString());
								hash.put("bot_name", bot_name);
								hashmap.put("bot_name", bot_name);
								
								
							} else {
								JSONObject contractJson = (JSONObject) contract.get(0);

								hash.put("price", contractJson.get("price").toString()); 
								hash.put("tot_price", contractJson.get("total").toString()); 
								hash.put("units", contractJson.get("units").toString());
								hash.put("fee", contractJson.get("fee").toString());
								hash.put("side", "ask");
								
								BigDecimal getTotPrice = new BigDecimal(sysorderlistvo.getTot_price());
								BigDecimal revenue = new BigDecimal(contractJson.get("total").toString());
								revenue = revenue.subtract(getTotPrice);
								revenue = revenue.divide(getTotPrice,2, RoundingMode.FLOOR);
								
								hash.put("revenue", revenue.toString());
								hash.put("bot_name", bot_name);
								hashmap.put("bot_name", bot_name);
								
								
							}
							
							hashmap.put("position", "Exit");
							scriptDao.bithumb_orderlist_insert(hash);
							scriptService.updateSysBotCk_sell(hashmap);
							System.out.println("빗썸 매도 해쉬맵 " + hashmap);
						}
					}
					System.out.println(m_id + "빗썸 매도");
				}
			} else {
				System.out.println(m_id + "빗썸 봇체크 안되어잇음");
			}

		}
	}

	@Override
	@Async
	public void upbitOrder(List<SysBotCkVo2> sysBotList_upbit, String exit_side, HashMap<String, String> hashmap2)
			throws ParseException {
		// System.out.println(hash);
		for (int i = 0; i < sysBotList_upbit.size(); i++) {
			if (!sysBotList_upbit.isEmpty()) {
				String currency = "";
				List<ApiVo> apiList = new ArrayList<>();
				HashMap<String, String> hash = new HashMap<String, String>(); // Api_tbl에서 apiKey, secretKey, currency
																				// 담는
																				// hashmap
				HashMap<String, String> apiHash = new HashMap<String, String>();
				HashMap<String, String> idHash = new HashMap<String, String>();
				apiHash.put("m_id", sysBotList_upbit.get(i).getM_id());
				apiHash.put("site", sysBotList_upbit.get(i).getSite());

				idHash.put("m_id", apiHash.get("m_id"));
				idHash.put("site", apiHash.get("site"));
				hashmap2.put("site", apiHash.get("site"));

				System.out.println("업비트 해시맵"+hashmap2);
				System.out.println("업비트 사이트 "+apiHash.get("site"));
				hashmap2.put("bot_name", sysBotList_upbit.get(i).getBot_name());
				System.out.println(sysBotList_upbit.get(i).getBot_name());
				System.out.println("업비트 해시맵"+hashmap2);
				
				
				
				//세팅값 , 봇이름
				double set_dou =Double.valueOf(sysBotList_upbit.get(i).getSet_per());
				
				apiList = scriptService.getApi(apiHash);
				currency = sysBotList_upbit.get(i).getCurrency();

				for (int j = 0; j < apiList.size(); j++) {
					String site = apiList.get(i).getSite();
					System.out.println("업비트 에이피아이 사이트 "+site);
					hash.put("apiKey", apiList.get(i).getApiKey());
					hash.put("secretKey", apiList.get(i).getSecretKey());
					hash.put("m_id", apiList.get(i).getM_id());
					hash.put("site", apiList.get(i).getSite());
					hash.put("currency", currency);

				}

				JSONParser parser = new JSONParser();
				Object obj;
				JSONObject jsonObject;
				JSONArray jsonArray;

				try {

					String balance_result = upbitApiService.getBalance(hash);

					obj = parser.parse(balance_result);
					jsonObject = (JSONObject) obj;
					JSONObject balance_data_krw = (JSONObject) jsonObject.get("bid_account"); // 사용가능 코인
					JSONObject balance_data = (JSONObject) jsonObject.get("ask_account"); // 사용가능 코인

					String available_krw = (String) balance_data_krw.get("balance");
					String available_coin = (String) balance_data.get("balance"); // 사용가능 코인

					if (exit_side.equals("Buy")) {
						double c_krw = Double.valueOf(available_krw) * set_dou; // 내 보유자산의 50%
						double price = Math.floor(c_krw * 100000000) / 100000000;

						hash.put("price", String.valueOf(price));

						String upbitresult =upbitApiService.marketBuyOrder(hash);
						
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(upbitresult);
						String uuid = (String) jsonObj.get("uuid");
		                
						hash.put("uuid", uuid);

						System.out.println(upbitresult);
//						hash.put("uuid", "50892a0f-cd38-4e75-987a-bfd51562bc5a");
						upbitApiService.getOrderDetail(hash);

						//여기부터
						String orderdetailString = upbitApiService.getOrderDetail(hash);

						// 디테일 파서 시작
						JSONParser jsonParse11 = new JSONParser();

						// JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
						JSONObject jsonObj11 = (JSONObject) jsonParse11.parse(orderdetailString);

//						while (jsonObj11.get("state").equals("wait")) {
//							orderdetailString = upbitApiService.getOrderDetail(hash);
//							JSONObject jsonObj2 = (JSONObject) jsonParse11.parse(orderdetailString);
//							if(!jsonObj2.get("state").equals("wait")) {
//								break;
//							}
//							System.out.println("상태 : "+jsonObj11.get("state"));
//							Thread.sleep(2000);
//							System.out.println("기다리는중");
//						}

						System.out.println("디테일");
						//여기부터
						orderdetailString = upbitApiService.getOrderDetail(hash);

						System.out.println(orderdetailString);

						// 디테일 파서 시작
						JSONParser jsonParse1 = new JSONParser();

						// JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
						JSONObject jsonObj1 = (JSONObject) jsonParse1.parse(orderdetailString);

						// JSONObject에서 PersonsArray를 get하여 JSONArray에 저장한다.
						JSONArray tradesArray = (JSONArray) jsonObj1.get("trades");

						JSONObject tempObj2 = (JSONObject) tradesArray.get(0);

						System.out.println("/*//");
						double sum = 0;
						double usum = 0;
						double tot_price = 0;
						double[] upprice = new double[tradesArray.size()];
						double[] upunits = new double[tradesArray.size()];
						double[] upfunds = new double[tradesArray.size()];
						String fee = (String) jsonObj1.get("paid_fee");

						if (tradesArray.size() == 1) {
							for (int i1 = 0; i1 < tradesArray.size(); i1++) {
								JSONObject tempObj = (JSONObject) tradesArray.get(i1);
								System.out.println("" + (i1 + 1) + "마켓: " + tempObj.get("market"));
								System.out.println("" + (i1 + 1) + "오더아이디 : " + jsonObj1.get("uuid"));
								System.out.println("" + (i1 + 1) + "볼륨 : " + tempObj.get("volume"));
								System.out.println("" + (i1 + 1) + "사이드 : " + tempObj.get("side"));
								System.out.println("" + (i1 + 1) + "매수가 : " + tempObj.get("price"));
								System.out.println("" + (i1 + 1) + "총액 : " + tempObj.get("funds"));
								System.out.println("" + (i1 + 1) + "수수료 : " + jsonObj1.get("paid_fee"));

								HashMap<String, String> deHash = new HashMap<String, String>();
								deHash.put("m_id", idHash.get("m_id"));
								deHash.put("site", idHash.get("site"));
								deHash.put("market", hash.get("currency"));
								deHash.put("uuid", (String) jsonObj1.get("uuid"));
								deHash.put("volume", (String) tempObj.get("volume"));
								deHash.put("side", (String) tempObj.get("side"));
								deHash.put("price", (String) tempObj.get("price"));
								deHash.put("funds", (String) tempObj.get("funds"));
								deHash.put("paid_fee", (String) jsonObj1.get("paid_fee"));
								deHash.put("revenue", "0");
								deHash.put("bot_name", sysBotList_upbit.get(i).getBot_name());
								
								scriptDao.setOrderListTbl(deHash);
								System.out.println("----------------------------");

							}
						} else {

							for (int i1 = 0; i1 < tradesArray.size(); i1++) {
								JSONObject tempObj = (JSONObject) tradesArray.get(i1);
								System.out.println("" + (i1 + 1) + "번째 멤버의 이름 : " + tempObj.get("market"));
								System.out.println("" + (i1 + 1) + "번째 멤버의 수량 : " + tempObj.get("volume"));
								System.out.println("----------------------------");
								upprice[i1] = Double.valueOf((String) tempObj.get("price"));
								upunits[i1] = Double.valueOf((String) tempObj.get("volume"));
								upfunds[i1] = Double.valueOf((String) tempObj.get("funds"));

							}
							for (int i1 = 0; i1 < upprice.length; i1++) {
								sum += upprice[i1];
								usum += upunits[i1];
								tot_price += upfunds[i1];
							}
							sum  = sum/ upprice.length ;
							String.valueOf(sum);
							
							HashMap<String, String> deHash = new HashMap<String, String>();
							deHash.put("m_id", idHash.get("m_id"));
							deHash.put("site", idHash.get("site"));
							deHash.put("market", hash.get("currency"));
							deHash.put("uuid", (String) jsonObj1.get("uuid"));
							deHash.put("volume", String.valueOf(usum));
							deHash.put("side", (String) jsonObj1.get("side"));
							deHash.put("price", (String) String.valueOf(sum));
							deHash.put("funds", (String) jsonObj1.get("price"));
							deHash.put("paid_fee", (String) jsonObj1.get("paid_fee"));
							deHash.put("revenue", "0");
							deHash.put("bot_name", sysBotList_upbit.get(i).getBot_name());
							scriptDao.setOrderListTbl(deHash);
						}
						////여기까지
						// String upbitresult =upbitApiService.marketBuyOrder(hash);
						// System.out.println(upbitresult);

						///////////////////////////////
						hashmap2.put("position", "Buy");
						scriptService.updateSysBotCk_buy(hashmap2);

						System.out.println("업비트 매수" + hashmap2);

					} else if (exit_side.equals("Exit")) {
						HashMap<String, String> getHash = new HashMap<String, String>();
						double c_coin = Double.valueOf(available_coin); // 내 보유 코인의 100%
						double units = Math.floor(c_coin * 100000000) / 100000000;
						getHash.put("m_id", idHash.get("m_id"));
						getHash.put("currency", hash.get("currency"));
						
						SysOrderlistVo sysorderlistvo = scriptDao.upbit_orderlist_select(getHash);
						SysOrderlistVo sysOrderlistVo = scriptDao.getOrderListTbl(getHash);
						
						System.out.println("currency : "+sysOrderlistVo.getCurrency());
						System.out.println("units : "+sysOrderlistVo.getUnits());
						
						hash.put("units", sysOrderlistVo.getUnits());
//						hash.put("units", String.valueOf(units));

						String upbitresult = upbitApiService.marketSellOrder(hash);
						System.out.println("매도완 : " + upbitresult);
						
						
						JSONParser jsonParser = new JSONParser();
						JSONObject jsonObj = (JSONObject) jsonParser.parse(upbitresult);
						String uuid = (String) jsonObj.get("uuid");
						hash.put("uuid", uuid);
//						hash.put("uuid", "20e49811-d112-4afb-977d-ede1f505e846");
						upbitApiService.getOrderDetail(hash);
						
						//여기부터
						String orderdetailString = upbitApiService.getOrderDetail(hash);

						// 디테일 파서 시작
						JSONParser jsonParse11 = new JSONParser();

						// JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
						JSONObject jsonObj11 = (JSONObject) jsonParse11.parse(orderdetailString);

						System.out.println("***************");
						System.out.println(orderdetailString);
						System.out.println("***************");
						
						while (jsonObj11.get("state").equals("wait")) {
							orderdetailString = upbitApiService.getOrderDetail(hash);
							System.out.println("상태 : "+jsonObj11.get("state"));
							Thread.sleep(2000);
							System.out.println("기다리는중");
						}

						System.out.println("디테일");
						System.out.println(orderdetailString);

						// 디테일 파서 시작
						JSONParser jsonParse1 = new JSONParser();

						// JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
						JSONObject jsonObj1 = (JSONObject) jsonParse1.parse(orderdetailString);

						// JSONObject에서 PersonsArray를 get하여 JSONArray에 저장한다.
						JSONArray tradesArray = (JSONArray) jsonObj1.get("trades");

						System.out.println("trade" + tradesArray);

						System.out.println(tradesArray.get(0));
						JSONObject tempObj2 = (JSONObject) tradesArray.get(0);
						System.out.println("출력하기 detail");
						System.out.println(tempObj2.get("price"));
						System.out.println(tempObj2.get("funds"));
						System.out.println(tempObj2.get("uuid"));

						System.out.println("/*//");
						double sum = 0;
						double usum = 0;
						double tot_price = 0;
						double[] upprice = new double[tradesArray.size()];
						double[] upunits = new double[tradesArray.size()];
						double[] upfunds = new double[tradesArray.size()];
						String fee = (String) jsonObj1.get("paid_fee");

						if (tradesArray.size() == 1) {
							for (int i1 = 0; i1 < tradesArray.size(); i1++) {
								JSONObject tempObj = (JSONObject) tradesArray.get(i1);
								System.out.println("" + (i1 + 1) + "마켓: " + tempObj.get("market"));
								System.out.println("" + (i1 + 1) + "오더아이디 : " + jsonObj1.get("uuid"));
								System.out.println("" + (i1 + 1) + "볼륨 : " + tempObj.get("volume"));
								System.out.println("" + (i1 + 1) + "사이드 : " + tempObj.get("side"));
								System.out.println("" + (i1 + 1) + "매수가 : " + tempObj.get("price"));
								System.out.println("" + (i1 + 1) + "총액 : " + tempObj.get("funds"));
								System.out.println("" + (i1 + 1) + "수수료 : " + jsonObj1.get("paid_fee"));

								HashMap<String, String> deHash = new HashMap<String, String>();
								deHash.put("m_id", idHash.get("m_id"));
								deHash.put("site", idHash.get("site"));
								deHash.put("market", hash.get("currency"));
								deHash.put("uuid", (String) jsonObj1.get("uuid"));
								deHash.put("volume", (String) tempObj.get("volume"));
								deHash.put("side", (String) tempObj.get("side"));
								deHash.put("price", (String) tempObj.get("price"));
								deHash.put("funds", (String) tempObj.get("funds"));
								deHash.put("paid_fee", (String) jsonObj1.get("paid_fee"));
								double iprice = Double.valueOf(sysorderlistvo.getPrice());
								double jprice = Double.valueOf((String)tempObj.get("price"));
								//units계산하기
								  NumberFormat format_8 = NumberFormat.getInstance();
								  format_8.setMaximumFractionDigits(8);
								  format_8.setRoundingMode(RoundingMode.HALF_EVEN);
								
								double revenue = Math.floor(((jprice-iprice)/iprice)*10000)/10000;
								String finalCoinprice = format_8.format(new BigDecimal(revenue)).replace(",", "");
								deHash.put("revenue",finalCoinprice);
								deHash.put("bot_name", sysBotList_upbit.get(i).getBot_name());
								scriptDao.setOrderListTbl(deHash);
								System.out.println("----------------------------");

							}
						} else {

							for (int i1 = 0; i1 < tradesArray.size(); i1++) {
								JSONObject tempObj = (JSONObject) tradesArray.get(i1);
								System.out.println("" + (i1 + 1) + "번째 멤버의 이름 : " + tempObj.get("market"));
								System.out.println("" + (i1 + 1) + "번째 멤버의 수량 : " + tempObj.get("volume"));
								System.out.println("----------------------------");
								upprice[i1] = Double.valueOf((String) tempObj.get("price"));
								upunits[i1] = Double.valueOf((String) tempObj.get("volume"));
								upfunds[i1] = Double.valueOf((String) tempObj.get("funds"));

							}
							for (int i1 = 0; i1 < upprice.length; i1++) {
								sum += upprice[i1];
								usum += upunits[i1];
								tot_price += upfunds[i1];
							}
							sum  = sum/ upprice.length ;
							String.valueOf(sum);
							
						   NumberFormat format_8 = NumberFormat.getInstance();
						   format_8.setMaximumFractionDigits(8);
						   format_8.setRoundingMode(RoundingMode.HALF_EVEN);
							  
							HashMap<String, String> deHash = new HashMap<String, String>();
							deHash.put("m_id", idHash.get("m_id"));
							deHash.put("site", idHash.get("site"));
							deHash.put("market", hash.get("currency"));
							deHash.put("uuid", (String) jsonObj1.get("uuid"));
							deHash.put("volume", String.valueOf(usum));
							deHash.put("side", (String) jsonObj1.get("side"));
							deHash.put("price", (String) String.valueOf(sum));
							deHash.put("funds", (String) jsonObj1.get("price"));
							
							//매수가
							double iprice = Double.valueOf(sysorderlistvo.getPrice());
							//매도가
							double jprice = Double.valueOf((String)String.valueOf(sum));
							
							double revenue = Math.floor(((jprice-iprice)/iprice)*100000000)/100000000;
							String revenueStr = format_8.format(new BigDecimal(revenue)).replace(",", "");
							deHash.put("revenue", revenueStr);
							
							deHash.put("paid_fee", (String) jsonObj1.get("paid_fee"));
							deHash.put("bot_name", sysBotList_upbit.get(i).getBot_name());
							scriptDao.setOrderListTbl(deHash);
						}
						////여기까지
						
						
						hashmap2.put("position", "Exit");
						scriptService.updateSysBotCk_sell(hashmap2);
						System.out.println("업비트 매도");
						System.out.println("업비트 매도"+hashmap2);
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("업비트 봇체크 안되어잇음");
			}
		}

	}

}
