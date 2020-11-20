package com.spring.bithumb.api.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.spring.bithumb.api.Api_Client;
import com.spring.bithumb.api.service.ApiService;

@Service("candleService")
public class ApiServiceImpl implements ApiService{
	
	String coin;
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
	//String ticker = "/public/ticker/";
	
	//Api_Client api2 = new Api_Client("","");
	HashMap<String, String> rgParams = new HashMap<String, String>();
	HashMap<String, String> rgParams2 = new HashMap<String, String>();
	HashMap<String, String> stick = new HashMap<String, String>();
	
	/*
	 * Api_Client api = new Api_Client("802f4dce5c52e55ab1ece1c772717188",
	 * "7f74c2b5a04107c722b1663f79538ffb");
	 */
	 

	@Override
	public String placebuy(String coin, String price, String units, 
			String apiKey, String secretKey) {
		HashMap<String, String> rgParams = new HashMap<String, String>();
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", units);
		rgParams.put("price", price);
		rgParams.put("type", "bid");
		
		String result = api2.callApi(place, rgParams);
		//System.out.println(result);
		return result;
	}

	@Override
	public String placesell(String coin, String price, String units, 
			String apiKey, String secretKey) {
		HashMap<String, String> rgParams = new HashMap<String, String>();
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", units);
		rgParams.put("price", price);
		rgParams.put("type", "ask");
		String result = api2.callApi(place, rgParams);
		
		return result;
	}
	
	@Override
	public String marketbuy(String coin,String units,String apiKey, String secretKey) {
		HashMap<String, String> rgParams = new HashMap<String, String>();
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		rgParams.put("units", units);
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		String result = api2.callApi(marketBuy, rgParams);
		
		return result;
	}
	
	@Override
	public String marketsell(String coin,String units,String apiKey, String secretKey) {
		HashMap<String, String> rgParams = new HashMap<String, String>();
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		rgParams.put("units", units);
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		String result = api2.callApi(marketSell, rgParams);
		
		return result;
	}
	
	@Override
	public String showOrderDetail(String coin, String order_id, String apiKey, String secretKey) {
		HashMap<String, String> rgParams2 = new HashMap<String, String>();
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		rgParams2.put("order_id", order_id);
		rgParams2.put("order_currency", coin);
		rgParams2.put("payment_currency", "KRW");
		
		String result = api2.callApi(order_detail, rgParams2);
		//System.out.println(result);
		return result;
		
	}
	
	@Override
	public String showOrder(String coin,String apiKey, String secretKey) { // api키 필요
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		
		HashMap<String, String> rgParams2 = new HashMap<String, String>();
		
		rgParams2.put("order_currency", coin);
		String result = api2.callApi(orders, rgParams2);
		//System.out.println(result);
		return result;
	}


	@Override
	public String showTicker(String coin) {
		Api_Client api2 = new Api_Client("","");
		//rgParams.put("order_currency", coin);
		//rgParams.put("payment_currency", "KRW");
		//result = api.callApi("/public/orderbook/"+currency+"_KRW?count="+100, rgParams);
		String result = api2.callApi2_Post("/public/ticker/"+coin+"_KRW", rgParams);
		//System.out.println(result);
		
		return result;
	}
	
	
	@Override
	public String showBalance(String coin,String apiKey, String secretKey) {
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		rgParams.put("currency", coin);
		
		String result = api2.callApi(balance, rgParams);
		return result;
	}
	
	
	@Override
	public String cancel(String order_id, String type,String coin,String apiKey, String secretKey) {
		Api_Client api2 = new Api_Client(apiKey,secretKey);
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("order_id", order_id);
		rgParams.put("type", type);
		String result = api2.callApi(cancel, rgParams);
		return result;
	}
	
	
}
