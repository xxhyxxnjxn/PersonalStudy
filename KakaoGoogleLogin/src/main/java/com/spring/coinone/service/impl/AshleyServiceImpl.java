package com.spring.coinone.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.spring.coinone.dao.AshleyDao;
import com.spring.coinone.service.AshleyService;
import com.spring.coinoneh.api.service.CoinoneApiService;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.BotSignVO;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysOrderlistVo;

@Service("ashleyService")
public class AshleyServiceImpl implements AshleyService {
	

	@Autowired
	private CoinoneApiService coinoneApiSerivce;
	
	@Autowired
	private AshleyDao ashleyDao;
	
	JSONObject obj = new JSONObject();
	JSONArray array = new JSONArray();
	JSONParser parser = new JSONParser();
	
	@Override
	@Async
	public void coinoneOrder(HashMap<String, String> hash) {
		// 시그널 번호가 같은 row를 조회
		 List<BotSignVO> showBotSign =  ashleyDao.showBotSign(hash);
  	  
		  for (int i = 0; i < showBotSign.size(); i++) {
			  BotSignVO showBotSignVO = showBotSign.get(i);
			  String bot_mid = showBotSignVO.getM_id();
			  String bot_site = showBotSignVO.getSite();
			  String bot_currency = showBotSignVO.getCurrency();
			  String bot_ck = showBotSignVO.getBot_ck();
			  String bot_scriptNo = showBotSignVO.getScriptNo();
			  String bot_position = showBotSignVO.getPosition();
			  String bot_name = showBotSignVO.getBot_name();
			  String set_per = showBotSignVO.getSet_per();
			  
			  //해당 업데이트 되는 회원의 id, site로 apikey를 가져옴.
			  HashMap <String, Object> getApiMap = new HashMap<String, Object>();
			  getApiMap.put("bot_mid",bot_mid);
			  getApiMap.put("bot_site",bot_site);
			  getApiMap.put("bot_currency",bot_currency);
			  getApiMap.put("bot_name",bot_name);
			  List<ApiVo> keyVo =  ashleyDao.getMemKey(getApiMap);
			  System.out.println(getApiMap);
			  System.out.println(keyVo.get(0));
		// key를 받아서 site별 for문 돌리기(scriptNo, position=exit, bot_ck=1,site=coinone)	 
			  for (int j = 0; j < keyVo.size(); j++) {
				  ApiVo apiKeyvo = keyVo.get(j);
				  System.out.println(apiKeyvo);
				  String m_id = apiKeyvo.getM_id();
				  String site = apiKeyvo.getSite();
				  String apiKey = apiKeyvo.getApiKey();
				  String secretKey = apiKeyvo.getSecretKey();
				// key 저장
				  HashMap  hashmap = new HashMap();	//balance조회를 위한 hash
				  hashmap.put("apiKey",apiKey);
				  hashmap.put("secretKey",secretKey);
				  hashmap.put("currency",bot_currency);
				  System.out.println(hashmap);
				try {
					String balance_coinone;
					balance_coinone = coinoneApiSerivce.getBalance(hashmap);
					 obj = (JSONObject) parser.parse(balance_coinone);
					  JSONObject dataObject = (JSONObject) obj.get("krw");
					  System.out.println(dataObject);
					  String balance_krw_coinone = dataObject.get("avail").toString();
					  System.out.println(balance_krw_coinone);
					  
					  double balance_krw_double =  Math.floor(Double.valueOf(balance_krw_coinone)*Float.valueOf(set_per));	//잔액 ,  설정값
					  if(balance_krw_double >= 1000) {
						  
						  // 시장가 주문
						  HashMap  orderhash = new HashMap();
						  orderhash.put("currency",bot_currency);//orderbook 조회를 위한 hash
						  String orderbook = coinoneApiSerivce.getOrderBook(orderhash);
						  orderhash.put("apiKey",apiKey);
						  orderhash.put("secretKey",secretKey);
	    				  System.out.println(orderbook);
	    				  
	    				  JSONObject obj_orderbook = (JSONObject) parser.parse(orderbook);
	    				  JSONArray asks = (JSONArray) obj_orderbook.get("ask");
	    				  
	    				  String ask_orderbook = "";
					  
						  float fee = (float) (balance_krw_double * 0.001); // 코인원수수료 0.2%/0.1% 
						  String feeString  = String.format("%.0f",fee);
						  // 수수료(feeString) 절삭
						  //주문가능 한 잔액 가격
						  String avil_balance = String.valueOf((float) (balance_krw_double - Float.valueOf(feeString)));
	    					  //주문가능한 수량
						  int n  = 0;
	    				  float qty_sum = 0;
	    				  float price_sum = 0;
	    				  float tot_sum = 0;
	    				  float tot_avg = 0;
	    				  float comp = Float.valueOf(avil_balance);
	    				  
	    				  for (int k = 0; k < asks.size(); k++) {
	    					  JSONObject ask_list = (JSONObject) asks.get(k);
	    					  String price = ask_list.get("price").toString();
	    					  String qty = ask_list.get("qty").toString();
	    					  float gop = Float.valueOf(price)* Float.valueOf(qty);
	    					  qty_sum += Float.valueOf(qty);
	    					  tot_sum += gop;
	    					  price_sum += Float.valueOf(price);
	    					  if(gop < comp){
	    						  comp = comp - gop;
	    						  n++;	
	    					  }else {
	    						  ask_orderbook = price.toString();
	    						  
	    					  }
	    				  }// n+1가격으로 채결해야 시장가로 매수가 가능함.
	    				  System.out.println(ask_orderbook);
	    				  
	    				  String avil_unit ="";
	    				  if(Float.valueOf(ask_orderbook)>0) {
	    					  avil_unit =  String.valueOf((float) (balance_krw_double - Float.valueOf(feeString))/Float.valueOf(ask_orderbook));
	    					  System.out.println(avil_unit);
	    				  }else {
	    					  System.out.println("코인 상장 폐지");
	    				  }
	    				  
	    				  System.out.println(n);
	    				  JSONObject askOrder = (JSONObject) asks.get(n);	//  n+1번째 object 를 들고옴.
	    				  String askOrderPrice = askOrder.get("price").toString();
	    				  String qty = askOrder.get("qty").toString();
	    				  System.out.println(askOrderPrice);
	    				  
	    				//units계산하기
						  NumberFormat format_4 = NumberFormat.getInstance();
						  format_4.setMaximumFractionDigits(4);
						  format_4.setRoundingMode(RoundingMode.HALF_EVEN);
						  
						  String units = format_4.format(new BigDecimal(avil_unit));
						  orderhash.put("units",units);
						  orderhash.put("price",askOrderPrice);

						  String orderResult = coinoneApiSerivce.limitBuyOrder(orderhash);
						  
						  System.out.println("--------------------------------------------------------------------------------"+orderResult+"----------------------------");
						  JSONObject responseOrder = (JSONObject) parser.parse(orderResult);
						  
						  String result = responseOrder.get("result").toString();
						  String errorCode = responseOrder.get("errorCode").toString();
						  
						  NumberFormat format_8 = NumberFormat.getInstance();
						  format_8.setMaximumFractionDigits(8);
						  format_8.setRoundingMode(RoundingMode.HALF_EVEN);
						  
						  
						  //올바로 주문이 되었으면 
						  if(result.equals("success")) {
							  	  String orderId = responseOrder.get("orderId").toString();
		    					  String completed_orders  = coinoneApiSerivce.MyCompletedOrders(hashmap);
		    					  System.out.println("모든 주문완료 내역 "+completed_orders);
		    					  JSONObject obj_completed = (JSONObject) parser.parse(completed_orders);
		    					  JSONArray completedOrders = (JSONArray) obj_completed.get("completeOrders");
		    					  
		    					  float hap_qty = 0;
		    					  float hap_order = 0;
		    					  float hap_fee = 0;
		    					  String feeRate = "";
		    					  String timestamp = "";
		    					  String type = "";
		    					  float avg = 0;
		    					  String tot_price = "";
		    					  int num = 0;
		    					  float revenue = 0;
			    					  for (int k = 0; k < completedOrders.size(); k++) {
			    						  JSONObject completedOrder = (JSONObject) completedOrders.get(k);
			    						  String completed_orderId = completedOrder.get("orderId").toString();
			    						  String completed_price = completedOrder.get("price").toString();
			    						  String completed_qty = completedOrder.get("qty").toString();
			    						  String completed_fee = completedOrder.get("fee").toString();
			    						  timestamp = completedOrder.get("timestamp").toString();
			    						  type = completedOrder.get("type").toString();
			    						  
			    						  
			    						  if(orderId.equals("orderId")) {
			    							  float price_order = Float.valueOf(completed_price);
			    							  float qty_order = Float.valueOf(completed_qty);
			    							  float fee_order = Float.valueOf(completed_fee);
			    							  float gop = price_order*qty_order;
			    							  hap_order += gop;
			    							  hap_qty += qty_order;
			    							  hap_fee += fee_order;
			    							  type = completedOrder.get("type").toString();
			    							  feeRate = completedOrder.get("feeRate").toString();
			    							  timestamp = completedOrder.get("timestamp").toString();
			    							  num ++;
			    						  		}
			    					  	}
			    					  
			    					  System.out.println( num );
			    					  
			    					  if(num==0) {
			    						  JSONObject completedOrder = (JSONObject) completedOrders.get(0);
			    						  String completed_orderId = completedOrder.get("orderId").toString();
			    						  String completed_price = completedOrder.get("price").toString();
			    						  String completed_qty = completedOrder.get("qty").toString();
			    						  String completed_fee = completedOrder.get("fee").toString();
			    						  timestamp = completedOrder.get("timestamp").toString();
			    						  avg =  Float.valueOf(completed_price);
			    						  hap_fee =  Float.valueOf(completed_fee);
			    						  type = completedOrder.get("type").toString();
			    						  hap_qty =  Float.valueOf(completed_qty);
			    						  tot_price = String.valueOf(hap_qty*avg);
			    						  revenue = 0;
			    					  }else {
			    						  avg =  hap_order/hap_qty;
				    					  tot_price = String.valueOf(avg * hap_qty);
				    					  revenue = 0;
			    					  }
		    					  //==========DB저장======================================================================================================================
					    				HashMap orderMap = new HashMap();
					    				long timestampLong = Long.parseLong(timestamp);
				    				    Date date = new java.util.Date(timestampLong*1000L); 
				    				    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				    				    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
				    				    String formattedDate = sdf.format(date);
					    				
				   						  //DB sys_orderlist_tbl 에 저장 (신호 받고 빗썸에서 exit -> buy로 들어간거 ).
				   						  orderMap.put("apiKey",apiKey);
				   						  orderMap.put("secretKey",secretKey);
				   						  orderMap.put("m_id",getApiMap.get("bot_mid"));
				   						  orderMap.put("site",getApiMap.get("bot_site"));
				   						  orderMap.put("currency",getApiMap.get("bot_currency"));
				   						  orderMap.put("bot_name",getApiMap.get("bot_name"));
				   						  
				   						  String finalCoinprice = format_8.format(new BigDecimal(avg)).replace(",", "");
				   						  orderMap.put("price",finalCoinprice);
				   						  String finalQty = format_8.format(new BigDecimal(hap_qty)).replace(",", "");
				   						  orderMap.put("units",finalQty);
				   						  
				   						  orderMap.put("order_id",orderId);
				   						  
										  orderMap.put("side",type);
										  orderMap.put("revenue",revenue);
										  String final_price = String.valueOf(Math.floor(avg)*(Math.floor(hap_qty*10000)/10000));
										  final_price = String.valueOf(Math.floor(Float.valueOf(final_price)));
										  orderMap.put("tot_price",final_price);
										  orderMap.put("order_date",formattedDate);
										  
										  orderMap.put("fee",format_8.format(new BigDecimal(hap_fee)));
										  
										  ashleyDao.insertOrderlist(orderMap);
			   						  
			   						  //신호 업데이트
			   							HashMap<String, Object> hashUpdate0 = new HashMap<String,Object>();
			   							hashUpdate0.put("bot_mid",getApiMap.get("bot_mid") );
			   							hashUpdate0.put("bot_site",getApiMap.get("bot_site") );
			   							hashUpdate0.put("bot_currency",getApiMap.get("bot_currency") );
			   							hashUpdate0.put("bot_name",getApiMap.get("bot_name") );
			   							hashUpdate0.put("scriptNo",hash.get("scriptNo"));
			   							hashUpdate0.put("log", hash.get("log"));
			   							hashUpdate0.put("side_num", hash.get("side_num"));
			   							hashUpdate0.put("side", hash.get("side"));
			   							ashleyDao.UpdateBotSign(hashUpdate0);
	    					 //==========DB저장끝======================================================================================================================
						  
	    					  }else {
	    						  System.out.println("주문이 실패하였습니다. 관리자에게 문의 하세요. 에러코드 : "+errorCode);
	    					  }
					  }else {
						  System.out.println("잔액 부족");
					  }
				  
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		  }
		
	}
	
	
	@Override
	@Async
	public void coinoneOrderExit(HashMap<String, String> hash) {
		 List<BotSignVO> showBotSign =  ashleyDao.showBotSignExit(hash);
	  	  System.out.println("넘어왔지롱");
	  	  System.out.println(showBotSign);
		  for (int i = 0; i < showBotSign.size(); i++) {
			  BotSignVO showBotSignVO = showBotSign.get(i);
			  String bot_mid = showBotSignVO.getM_id();
			  String bot_site = showBotSignVO.getSite();
			  String bot_currency = showBotSignVO.getCurrency();
			  String bot_ck = showBotSignVO.getBot_ck();
			  String bot_scriptNo = showBotSignVO.getScriptNo();
			  String bot_position = showBotSignVO.getPosition();
			  String bot_name = showBotSignVO.getBot_name();
			  String set_per = showBotSignVO.getSet_per();
			  
			  //해당 업데이트 되는 회원의 id, site로 apikey를 가져옴.
			  HashMap <String, Object> getApiMap = new HashMap<String, Object>();
			  getApiMap.put("bot_mid",bot_mid);
			  getApiMap.put("bot_site",bot_site);
			  getApiMap.put("bot_currency",bot_currency);
			  getApiMap.put("bot_name",bot_name);
			  List<ApiVo> keyVo =  ashleyDao.getMemKey(getApiMap);
			  System.out.println(getApiMap);
			  System.out.println(keyVo.get(0));
		// key를 받아서 site별 for문 돌리기(scriptNo, position=exit, bot_ck=1,site=coinone)	 
			  for (int j = 0; j < keyVo.size(); j++) {
				  
				  ApiVo apiKeyvo = keyVo.get(j);
				  System.out.println(apiKeyvo);
				  String m_id = apiKeyvo.getM_id();
				  String site = apiKeyvo.getSite();
				  String apiKey = apiKeyvo.getApiKey();
				  String secretKey = apiKeyvo.getSecretKey();
				// key 저장
				  HashMap  hashmap = new HashMap();
				  hashmap.put("apiKey",apiKey);
				  hashmap.put("secretKey",secretKey);
				  hashmap.put("currency",getApiMap.get("bot_currency"));
				try {
					String balance_coinone;
					balance_coinone = coinoneApiSerivce.getBalance(hashmap);
					 obj = (JSONObject) parser.parse(balance_coinone);
					  JSONObject dataObject = (JSONObject) obj.get(bot_currency.toLowerCase());
					  String balance_coin = dataObject.get("avail").toString();
					  System.out.println(balance_coin);
					 
					  double balance_double =  Double.valueOf(balance_coin);	//잔액 ,  설정값
					  
					  if(balance_double >= 0) {
						  // 시장가 주문
						  HashMap  orderhash = new HashMap();
						  orderhash.put("currency",getApiMap.get("bot_currency"));
						  String orderbook = coinoneApiSerivce.getOrderBook(orderhash);
						  orderhash.put("apiKey",apiKey);
						  orderhash.put("secretKey",secretKey);
	    				  JSONObject obj_orderbook = (JSONObject) parser.parse(orderbook);
	    				  JSONArray bids = (JSONArray) obj_orderbook.get("bid");
	    			
	    				  String bid_orderbook = "";
	    				  
	    				  float fee = (float) (balance_double * 0.001); // 코인원수수료 0.2%/0.1% 
						  String feeString  = String.format("%.0f",fee);
						  
						  // 수수료(feeString) 절삭
						  //주문가능 한 잔액 가격
						  String avil_balance = String.valueOf((float) (balance_double));
	    					  //주문가능한 수량
						  int n  = 0;
	    				  float qty_sum = 0;
	    				  float price_sum = 0;
	    				  float tot_sum = 0;
	    				  float tot_avg = 0;
	    				  float comp = Float.valueOf(avil_balance); //수량
	    				  
	    				  for (int k = 0; k < bids.size(); k++) {
	    					  JSONObject bid_list = (JSONObject) bids.get(k);
	    					  String qty = bid_list.get("qty").toString();
	    					  String price = bid_list.get("price").toString();
	    					  qty_sum += Float.valueOf(qty);
	    					  float qtyf = Float.valueOf(qty);
	    					  if(qtyf < comp){
	    						  comp = comp - qtyf;
	    						  n++;	
	    					  }
	    				  }// n+1가격으로 채결해야 시장가로 매도가 가능함.
	    				  System.out.println(bid_orderbook);
	    				  
	    				  String avil_unit ="";
	    				  String rev_pricebefore = "";
	    				  HashMap<String , Object> getHash = new HashMap<String , Object>();
	    				  getHash.put("apiKey",apiKey);
	    				  getHash.put("secretKey",secretKey);
	    				  getHash.put("m_id",getApiMap.get("bot_mid"));
	    				  getHash.put("site",getApiMap.get("bot_site"));
	    				  getHash.put("currency",getApiMap.get("bot_currency"));
	    				  getHash.put("side","bid");
	    				  getHash.put("bot_name",getApiMap.get("bot_name"));

	    				  List<SysOrderlistVo> database_bal = ashleyDao.getAssetCoin(getHash);
	    				  
	    				  avil_unit = database_bal.get(0).getUnits().toString();
	    				  rev_pricebefore = database_bal.get(0).getPrice().toString();
	    				  System.out.println(avil_unit);
	    				  double arr [] = {Double.valueOf(avil_unit),Double.valueOf(avil_balance)};
	    				  Arrays.sort(arr);
	    				  avil_unit = String.valueOf(arr[0]);
	    				  
	    				  System.out.println(n);
	    				  JSONObject bidOrder = (JSONObject) bids.get(n+1);	//  n+1번째 object 를 들고옴.
	    				  String bidOrderPrice = bidOrder.get("price").toString();
	    				  System.out.println(bidOrderPrice);
	    				  
	    				//units계산하기
						  NumberFormat format_8 = NumberFormat.getInstance();
						  format_8.setMaximumFractionDigits(8);
						  format_8.setRoundingMode(RoundingMode.HALF_EVEN);
						  
						  String units = format_8.format(new BigDecimal(avil_unit));
						  
						  orderhash.put("units",units);
						  orderhash.put("price",bidOrderPrice);

						  String orderResult = coinoneApiSerivce.limitSellOrder(orderhash);
						  
						  System.out.println("--------------------------------------------------------------------------------"+orderResult+"----------------------------");
						  JSONObject responseOrder = (JSONObject) parser.parse(orderResult);
						  
						  String result = responseOrder.get("result").toString();
						  String errorCode = responseOrder.get("errorCode").toString();
						  
						  //올바로 주문이 되었으면 
						  if(result.equals("success")) {
							  
							  	  String orderId = responseOrder.get("orderId").toString();
							  	  
		    					  String completed_orders  = coinoneApiSerivce.MyCompletedOrders(hashmap);
		    					  System.out.println("이것도 소연이가 출력했어요~~~");
		    					  System.out.println(completed_orders);
		    					  JSONObject obj_completed = (JSONObject) parser.parse(completed_orders);
		    					  JSONArray completedOrders = (JSONArray) obj_completed.get("completeOrders");
		    					  
		    					  float hap_qty = 0;
		    					  float hap_order = 0;
		    					  float hap_fee = 0;
		    					  String feeRate = "";
		    					  String timestamp = "";
		    					  String type = "";
		    					  float avg = 0;
		    					  String tot_price = "";
		    					  int num = 0;
		    					  String revenue = ""; 
			    					  for (int k = 0; k < completedOrders.size(); k++) {
			    						  JSONObject completedOrder = (JSONObject) completedOrders.get(k);
			    						  String completed_orderId = completedOrder.get("orderId").toString();
			    						  String completed_price = completedOrder.get("price").toString();
			    						  String completed_qty = completedOrder.get("qty").toString();
			    						  String completed_fee = completedOrder.get("fee").toString();
			    						  timestamp = completedOrder.get("timestamp").toString();
			    						  type = completedOrder.get("type").toString();
			    						  
			    						  if(orderId.equals("orderId")) {
			    							  float price_order = Float.valueOf(completed_price);
			    							  float qty_order = Float.valueOf(completed_qty);
			    							  float fee_order = Float.valueOf(completed_fee);
			    							  float gop = price_order*qty_order;
			    							  hap_order += gop;
			    							  hap_qty += qty_order;
			    							  hap_fee += fee_order;
			    							  type = completedOrder.get("type").toString();
			    							  feeRate = completedOrder.get("feeRate").toString();
			    							  timestamp = completedOrder.get("timestamp").toString();
			    							  num ++;
			    						  		}
			    					  	}
			    					  System.out.println( num );
			    					  
			    					  if(num==0) {
			    						  JSONObject completedOrder = (JSONObject) completedOrders.get(0);
			    						  String completed_orderId = completedOrder.get("orderId").toString();
			    						  String completed_price = completedOrder.get("price").toString();
			    						  String completed_qty = completedOrder.get("qty").toString();
			    						  String completed_fee = completedOrder.get("fee").toString();
			    						  timestamp = completedOrder.get("timestamp").toString();
			    						  avg =  Float.valueOf(completed_price);
			    						  hap_fee =  Float.valueOf(completed_fee);
			    						  type = completedOrder.get("type").toString();
			    						  hap_qty =  Float.valueOf(completed_qty);
			    						  tot_price = String.valueOf(hap_qty*avg);
			    						  float rate = (avg - Float.valueOf(rev_pricebefore))/Float.valueOf(rev_pricebefore);
			    						  revenue = String.valueOf(Math.floor(rate*1000)/1000);
			    					  }else {
			    						  avg =  hap_order/hap_qty;
				    					  tot_price = String.valueOf(avg * hap_qty);
				    					  float rate = (avg - Float.valueOf(rev_pricebefore))/Float.valueOf(rev_pricebefore);
				    					  revenue = String.valueOf(Math.floor(rate*1000)/1000);
			    					  }
		    					 //평균단가
		    					 System.out.println(avg);
		    					 //총 매수수량
		    					 System.out.println(hap_qty);
		    					 //매수시 수수료
		    					 System.out.println(hap_fee);
		    					  //==========DB저장======================================================================================================================
					    				HashMap orderMap = new HashMap();
					    				
					    				  long timestampLong = Long.parseLong(timestamp);
					    				    Date date = new java.util.Date(timestampLong*1000L); 
					    				    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
					    				    sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
					    				    String formattedDate = sdf.format(date);
					    				
				   						  //DB sys_orderlist_tbl 에 저장 (신호 받고 빗썸에서 exit -> buy로 들어간거 ).
				   						  orderMap.put("apiKey",apiKey);
				   						  orderMap.put("secretKey",secretKey);
				   						  orderMap.put("m_id",getApiMap.get("bot_mid"));
				   						  orderMap.put("site",getApiMap.get("bot_site"));
				   						  orderMap.put("currency",getApiMap.get("bot_currency"));
				   						  orderMap.put("bot_name",getApiMap.get("bot_name"));
				   						  orderMap.put("side",type);
				   						  String finalCoinprice = format_8.format(new BigDecimal(avg)).replace(",", "");
				   						  orderMap.put("price",finalCoinprice);
				   						  String finalQty = format_8.format(new BigDecimal(hap_qty)).replace(",", "");
				   						  orderMap.put("units",finalQty);
				   						  String final_price = String.valueOf(Math.floor(avg)*(Math.floor(hap_qty*10000)/10000));
				   						  final_price = String.valueOf(Math.floor(Float.valueOf(final_price)));
										  orderMap.put("tot_price",final_price);
				   						  orderMap.put("order_id",orderId);
				   						  orderMap.put("revenue",revenue);
				   						  
										  orderMap.put("order_date",formattedDate);
										  
										  orderMap.put("fee",format_8.format(new BigDecimal(hap_fee)));
										  
										 ashleyDao.insertOrderlist(orderMap);
			   						  
			   						  //신호 업데이트
			   							HashMap<String, Object> hashUpdate0 = new HashMap<String,Object>();
			   							hashUpdate0.put("bot_mid",getApiMap.get("bot_mid") );
			   							hashUpdate0.put("bot_site",getApiMap.get("bot_site") );
			   							hashUpdate0.put("bot_currency",getApiMap.get("bot_currency") );
			   							hashUpdate0.put("bot_name",getApiMap.get("bot_name") );
			   							hashUpdate0.put("scriptNo",hash.get("scriptNo"));
			   							hashUpdate0.put("side_num", hash.get("side_num"));
			   							hashUpdate0.put("side", hash.get("side"));
			   							ashleyDao.UpdateBotSignExit(hashUpdate0);
			   							
	    					 //==========DB저장끝======================================================================================================================
	    					  }else {
	    						  System.out.println("주문이 실패하였습니다. 관리자에게 문의 하세요. 에러코드 : "+errorCode);
	    					  }
					  }else {
						  System.out.println("잔액 부족");
					  }
					
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			
			  }
		  }
		
	}


	

}
