package com.spring.api.bithumb.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.spring.api.bithumb.Api_Client;

@Service("candleService")
public class ApiServiceImpl implements ApiService{
	
	String candlestick = "/public/candlestick/";
	String account = "/info/account";
	String balance = "/info/balance"; 
	String wallet_address ="/info/wallet_address";
	String ticker = "/public/ticker/";
	String orderBook = "/public/orderbook";
	String transactionHistory="/public/transaction_history";
	String orders = "/info/orders";
	String order_detail = "/info/order_detail";
	String cancel = "/trade/cancel";
	String place = "/trade/place";
	String marketBuy = "/trade/market_buy";
	String marketSell = "/trade/market_sell";
	String transactions = "/info/user_transactions";
	
	HashMap<String, String> rgParams = new HashMap<String, String>();

	@Override
	public String getOrderBook(HashMap<String,String> hash) {
		Api_Client api = new Api_Client("","");
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		//rgParams.put("count", "5");
		
		String result="";
		
		try {
			result = api.callApi2("/public/orderbook/"+hash.get("currency")+"_KRW", rgParams);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getTicker(HashMap<String,String> hash) {
		Api_Client api = new Api_Client("","");
		
		String result = api.callApi2_Post("/public/ticker/"+hash.get("currency")+"_KRW", rgParams);
		
		return result;
	}
	
	@Override
	public String getBalance(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		rgParams.put("currency", hash.get("currency"));
		
		String result = api.callApi(balance, rgParams);
		return result;
	}
	
	@Override
	public String limitBuyOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		rgParams.put("price", hash.get("price"));
		rgParams.put("type", "bid");
		
		String result = api.callApi(place, rgParams);
		
		return result;
	}
	@Override
	public String limitSellOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		rgParams.put("price", hash.get("price"));
		rgParams.put("type", "ask");
		String result = api.callApi(place, rgParams);
		
		return result;
	}
	
	@Override
	public String marketBuyOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		
		String result = api.callApi(marketBuy, rgParams);
		return result;
	}
	@Override
	public String marketSellOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		
		String result = api.callApi(marketSell, rgParams);
		
		return result;
	}
	@Override
	public String cancelOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("order_id", hash.get("orderId"));
		rgParams.put("type", hash.get("type"));
		
		String result = api.callApi(cancel, rgParams);
		
		return result;
	}
	@Override
	public String getOrderDetail(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_id", hash.get("orderId"));
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		
		String result = api.callApi(order_detail, rgParams);
		
		return result;
	}
	@Override
	public String getOrderList(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		
		String result = api.callApi(orders, rgParams);
		
		return result;
	}
	@Override
	public JsonNode getKakaoAccessToken(String code) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getKakaoAccessToken(code);
		return result;
	}
	
	@Override
	public JsonNode getKakaoUserInfo(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getKakaoUserInfo(accessToken);
		return result;
	}
	@Override
	public JsonNode kakaoLogout(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.kakaoLogout(accessToken);
		return result;
	}
	@Override
	public JsonNode getGoogleAccessToken(String code) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getGoogleAccessToken(code);
		return result;
	}
	
	@Override
	public JsonNode getGoogleUserInfo(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getGoogleUserInfo(accessToken);
		return result;
	}
	
}
