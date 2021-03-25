package gmc.rd.report.api.upbit.service;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import gmc.rd.report.api.upbit.dao.UpbitApiDao;
import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.OrderDetail;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.api.upbit.vo.UpbitAccountVo;
import gmc.rd.report.api.upbit.vo.UpbitErrorVo;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;
import gmc.rd.report.api.upbit.vo.Withdraws;

@Service("UpbitApiService")
public class UpbitApiServiceImpl implements UpbitApiService {


	static String serverUrl = "https://api.upbit.com";
	static String result = "";
	
	String marketAll = "/v1/market/all?isDetails=false";
	String candleStick = "/v1/candles/minutes";
	String orderbook = "/v1/orderbook?markets=krw-";
	String ticker = "/v1/ticker?markets=krw-";
	String account = "/v1/accounts";
	String balance = "/v1/orders/chance?";
	String orderdetail = "/v1/order?";
	String trade = "/v1/orders";
	String withdraws = "/v1/withdraws?";
	String deposits = "/v1/deposits?";
	@Autowired
	UpbitApiDao upbitApiDao;
	// 전체 계좌 조회
	@Override
	public UpbitAccountVo getAccounts(HashMap<String, String> hashMap) {
		result = upbitApiDao.accountHttp(hashMap, account);
		Gson gson = new Gson();

		UpbitAccountVo upbitAccountVo= new UpbitAccountVo();
			
		List<UpbitAccountDataVo> upbitAccountDataVo = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(result);
			if(jsonNode.get("error")==null) {
				
				
				Type listType = new TypeToken<List<UpbitAccountDataVo>>(){}.getType();
				upbitAccountDataVo = gson.fromJson(result, listType);
				upbitAccountVo.setData(upbitAccountDataVo);
				
			}
			else {
			upbitAccountVo = gson.fromJson(result, UpbitAccountVo.class);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		Type listType = new TypeToken<List<UpbitAccountVo>>(){}.getType();
//		upbitAccountDataVo = gson.fromJson(result, listType);

//		//upbitAccountDataVo = upbitAccountVo.getError();
//		System.out.println("==");
//		System.out.println(upbitAccountDataVo);
	
		
		/*
		List<UpbitTransactionVo> transaction = null;
		
		Gson gson = new Gson();
		Type listType = new TypeToken<List<UpbitTransactionVo>>(){}.getType();
		transaction = gson.fromJson(result, listType);
*/
		
		
		return upbitAccountVo;

	}

	@Override
	public String getCandleStick(HashMap<String, String> hashMap) {

		
		result = upbitApiDao.publicHttp(hashMap, candleStick+"/60");

		return result;
	}
	
	@Override
	public String getOrderBook(HashMap<String, String> hashMap) {

		result = upbitApiDao.publicHttp(hashMap, orderbook);

		return result;
	}
	// 티커

	@Override
	public String getTicker(HashMap<String, String> hashMap) {
		result = upbitApiDao.publicHttp(hashMap, ticker);

		return result;
	}

	// 주문 가능

	@Override
	public String getBalance(HashMap<String, String> hashMap) throws Exception {
		
		HashMap<String, String> params = new HashMap<>();
		params.put("market", "KRW-"+hashMap.get("currency"));

		result = upbitApiDao.privateHttp(hashMap, params, balance);
		System.out.println("result 는 : " + result);

		return result;

	}

	// 개별 주문 조회

	@Override
	public String getOrderDetail(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uuid", hashMap.get("uuid"));

		result = upbitApiDao.privateHttp(hashMap, params, orderdetail);
		System.out.println("result 는 : " + result);
		
		List<Trades> trades = null;
		
		Gson gson = new Gson();
		OrderDetail orderDetail = gson.fromJson(result, OrderDetail.class);
		
		trades = orderDetail.getTrades();
		return result;
	}
	
	//개별주문조회 후 trades 값 뽑기
	@Override
	public List<Trades> getOrderDetail2(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uuid", hashMap.get("uuid"));
		
		while (true) {
			result = upbitApiDao.privateHttp(hashMap, params, orderdetail);
			//System.out.println("orderDetail result 는 : " + result);

			if (result.contains("Too many API requests.")) {
				Thread.sleep(2000);
				result = upbitApiDao.privateHttp(hashMap, params, orderdetail);
				//System.out.println("result 는 : " + result);
			} else {
				break;
			}
		}
		
		List<Trades> trades = null;
		
		Gson gson = new Gson();
		OrderDetail orderDetail = gson.fromJson(result, OrderDetail.class);
		
		trades = orderDetail.getTrades();
		return trades;
	}
	

	// 주문리스트

	@Override

	   public List<UpbitTransactionVo> getOrderList(HashMap<String, String> hashMap) throws Exception {
	      HashMap<String, String> params = new HashMap<String, String>();
	      params.put("state", hashMap.get("state"));
	      params.put("page", hashMap.get("page"));
	      //params.put("kind", "watch");

	      String[] states = {
//	            "done",
//	            "cancel"
	      };
	   	  	
			while (true) {
				 result = upbitApiDao.orderListHttp(hashMap, params, states);

				if (result.contains("Too many API requests.")) {
					Thread.sleep(2000);
					 result = upbitApiDao.orderListHttp(hashMap, params, states);
					 
					System.out.println("orderList result 는 : " + result);
				} else {
					break;
				}
			}
	      
	      //System.out.println("+++");
	      List<UpbitTransactionVo> transaction = null;
	      
	      
	      try {

	         Gson gson = new Gson();
	         Type listType = new TypeToken<List<UpbitTransactionVo>>(){}.getType();
	         transaction = gson.fromJson(result, listType);
	         if(transaction.isEmpty()) {
	            System.out.println("페이지 끝");
	         }else {
	            transaction.get(0).getState();
	          //  System.out.println("던이냐" + transaction.get(0).getState());
	          //  System.out.println("던의 아이디냐" + transaction.get(0).getUuid());
	            for (int j = 0; j < transaction.size(); j++) {
	               
//	               System.out.println("던의 객채냐 : "+transaction.get(j).getSide() + 
//	                     ", "+ transaction.get(j).getCreated_at() + 
//	                     ", "+ transaction.get(j).getExecuted_volume() + 
//	                     ", "+ transaction.get(j).getLocked() + 
//	                     ", "+ transaction.get(j).getMarket() + 
//	                     ", "+ transaction.get(j).getOrd_type() + 
//	                     ", "+ transaction.get(j).getPaid_fee() + 
//	                     ", "+ transaction.get(j).getPrice() + 
//	                     ", "+ transaction.get(j).getState() + 
//	                     ", "+ transaction.get(j).getVolume()  
//	                     
//	                     );
	            }
	         }
	         
	      }catch(NullPointerException e) {
	               e.printStackTrace();
	           }
	      
	      return transaction;
	   }

	@Override
	   public String getOrderList2(HashMap<String, String> hashMap) throws Exception {
	      HashMap<String, String> params = new HashMap<String, String>();
	      params.put("state", hashMap.get("state"));
	      params.put("page", hashMap.get("page"));
	      //params.put("kind", "watch");

	      String[] states = {
//	            "done",
//	            "cancel"
	      };
	   	  	
			while (true) {
				 result = upbitApiDao.orderListHttp(hashMap, params, states);

				if (result.contains("Too many API requests.")) {
					Thread.sleep(2000);
					 result = upbitApiDao.orderListHttp(hashMap, params, states);
					 
					//System.out.println("orderList result 는 : " + result);
				} else {
					break;
				}
			}
	      
	      return result;
	   }

	// 주문삭제 - 주문번호 수정해야함

	@Override
	public String cancelOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("uuid", hashMap.get("orderId"));

		result = upbitApiDao.cancelHttp(hashMap, params);
		

		return result;
	}

	// 지정가 매수

	@Override
	public String limiteBuyOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "bid");
		params.put("price", hashMap.get("price"));
		params.put("ord_type", "limit");

		result = upbitApiDao.tradeHttp(hashMap, params, trade);
		


		return result;
	}

	// 지정가 매도
	@Override
	public String limiteSellOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "ask");
		params.put("volume", hashMap.get("units"));
		params.put("ord_type", "limit");

		result = upbitApiDao.tradeHttp(hashMap, params, trade);
		

		return result;
	}
	// 시장가 매수

	@Override
	public String marketBuyOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "bid");
		params.put("price", hashMap.get("price"));
		params.put("ord_type", "price");

		result = upbitApiDao.tradeHttp(hashMap, params, trade);
		

		return result;
	}

	// 시장가 매도

	@Override
	public String marketSellOrder(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("market", "KRW-" + hashMap.get("currency"));
		params.put("side", "ask");
		params.put("volume", hashMap.get("units"));
		params.put("ord_type", "market");

		result = upbitApiDao.tradeHttp(hashMap, params, trade);
		
		return result;
	}

	@Override
	public String getWithdraws(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("currency", hashMap.get("currency"));
		params.put("page", hashMap.get("page"));
		

		while (true) {
			result = upbitApiDao.withdrawHttp(hashMap, params);
			
			if (result.contains("Too many API requests.")) {
				Thread.sleep(2000);
				result = upbitApiDao.withdrawHttp(hashMap, params);
				//System.out.println("result 는 : " + result);
			} else {
				break;
			}

		}
		
		
//		if(result.equals("[]")) {
//			return null;
//		}else {
//		System.out.println("withdraws 값" + result);
//		Gson gson = new Gson();
//		List<Withdraws> withdraws = null;
//        Type listType = new TypeToken<List<Withdraws>>(){}.getType();
//        withdraws = gson.fromJson(result, listType);
//		return result;
//		}
		return result;
	}
	
	@Override
	public Withdraws getWithdrawsError(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("currency", hashMap.get("currency"));
		params.put("page", hashMap.get("page"));
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		
		Withdraws withdrawsVo = new Withdraws();
		

		result = upbitApiDao.withdrawHttp(hashMap, params);

		jsonNode = mapper.readTree(result);

		if (jsonNode.get("error") != null) {
			Gson gson = new Gson();
			withdrawsVo = gson.fromJson(result, Withdraws.class);
			//System.out.println(withdrawsVo);
		}

		return withdrawsVo;
	}

	@Override
	public String getDeposits(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("currency", hashMap.get("currency"));
		params.put("page", hashMap.get("page"));
		while (true) {
			result = upbitApiDao.depositHttp(hashMap, params);

			if (result.contains("Too many API requests.")) {
				Thread.sleep(2000);
				result = upbitApiDao.depositHttp(hashMap, params);
				//System.out.println("result 는 : " + result);
			} else {
				break;
			}
		}

//		if(result.equals("[]")) {
//			return null;
//		}else {
//			System.out.println("deposits 값" + result);
//			Gson gson = new Gson();
//			List<Deposit> deposit = null;
//	        Type listType = new TypeToken<List<Deposit>>(){}.getType();
//	        deposit = gson.fromJson(result, listType);
//			return deposit;
//		}
		return result;
	}
	
	@Override
	public Deposit getDepositsError(HashMap<String, String> hashMap) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("currency", hashMap.get("currency"));
		params.put("page", hashMap.get("page"));

		result = upbitApiDao.depositHttp(hashMap, params);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		
		jsonNode = mapper.readTree(result);
		
		Deposit depositVo = new Deposit();
		
		if (jsonNode.get("error") != null) {
			Gson gson = new Gson();
			depositVo = gson.fromJson(result, Deposit.class);
			//System.out.println(withdrawsVo);
		}

		return depositVo;
	}
	
	@Override
	public List<MarketAll> getMarketAll() {
		result = upbitApiDao.publicHttp2(marketAll);
		//System.out.println("마켓올에 대한 결과값 : "+result);
		
		Gson gson = new Gson();
		List<MarketAll> marketAll = null;
        Type listType = new TypeToken<List<MarketAll>>(){}.getType();
        marketAll = gson.fromJson(result, listType);
		
		for (int i = 0; i < marketAll.size(); i++) {
			if(marketAll.get(i).getMarket().contains("KRW-")) {
			marketAll.get(i).setMarket(marketAll.get(i).getMarket().replace("KRW-",""));
			}else if(marketAll.get(i).getMarket().contains("USDT-")) {
				marketAll.get(i).setMarket(marketAll.get(i).getMarket().replace("USDT-",""));
				marketAll.remove(i);
				i--;
			}else if(marketAll.get(i).getMarket().contains("BTC-")) {
				marketAll.get(i).setMarket(marketAll.get(i).getMarket().replace("BTC-",""));
				marketAll.remove(i);
				i--;
			}
		}
        
		//System.out.println("withdraws.get(0).getCurrency() : "+marketAll.get(0).getMarket());
		return marketAll;
	}

}