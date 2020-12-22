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
//import com.spring.api.bithumb.vo.BithumbDataVo;
//import com.spring.api.bithumb.vo.BithumbTransactionVo;

import gmc.rd.report.api.upbit.dao.UpbitApiDao;
import gmc.rd.report.api.upbit.vo.OrderDetail;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.api.upbit.vo.UpbitAccountVo;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;

@Service("UpbitApiService")
public class UpbitApiServiceImpl implements UpbitApiService {


	static String serverUrl = "https://api.upbit.com";
	static String result = "";
	
	String orderbook = "/v1/orderbook?markets=krw-";
	String ticker = "/v1/ticker?markets=krw-";
	String account = "/v1/accounts";
	String balance = "/v1/orders/chance?";
	String orderdetail = "/v1/order?";
	String trade = "/v1/orders";
	
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

		result = upbitApiDao.privateHttp(hashMap, params, orderdetail);
		System.out.println("result 는 : " + result);
		
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
		params.put("state", "done");
		//params.put("kind", "watch");

		String[] uuids = {
				//"388d8ad5-4879-4efb-b7d7-f0223918bedb"
		};

		result = upbitApiDao.orderListHttp(hashMap, params, uuids);
		System.out.println("cencel 로 넘긴것"+ result+" : 입니다.");
		System.out.println("+++");
		List<UpbitTransactionVo> transaction = null;
		
		try {

			Gson gson = new Gson();
			Type listType = new TypeToken<List<UpbitTransactionVo>>(){}.getType();
			transaction = gson.fromJson(result, listType);
			transaction.get(0).getState();
			System.out.println("던이냐" + transaction.get(0).getState());
			System.out.println("던의 아이디냐" + transaction.get(0).getUuid());
			for (int j = 0; j < transaction.size(); j++) {
				
				System.out.println("던의 객채냐 : "+transaction.get(j).getSide() + 
						", "+ transaction.get(j).getCreated_at() + 
						", "+ transaction.get(j).getExecuted_volume() + 
						", "+ transaction.get(j).getLocked() + 
						", "+ transaction.get(j).getMarket() + 
						", "+ transaction.get(j).getOrd_type() + 
						", "+ transaction.get(j).getPaid_fee() + 
						", "+ transaction.get(j).getPrice() + 
						", "+ transaction.get(j).getState() + 
						", "+ transaction.get(j).getVolume()  
						
						);
			}
		}catch(NullPointerException e) {
	            e.printStackTrace();
	        }
		
		return transaction;
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

}