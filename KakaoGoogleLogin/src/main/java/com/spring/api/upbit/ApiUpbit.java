package com.spring.api.upbit;

import java.util.HashMap;

public interface ApiUpbit {
	String getAccounts(HashMap<String, String> hashMap);

	String getOrderBook(HashMap<String, String> hashMap);

	String getTicker(HashMap<String, String> hashMap);

	String marketSellOrder(HashMap<String, String> hashMap) throws Exception;

	String marketBuyOrder(HashMap<String, String> hashMap) throws Exception;

	String limiteSellOrder(HashMap<String, String> hashMap) throws Exception;

	String limiteBuyOrder(HashMap<String, String> hashMap) throws Exception;

	String cancelOrder(HashMap<String, String> hashMap) throws Exception;

	String getOrderList(HashMap<String, String> hashMap) throws Exception;

	String getOrderDetail(HashMap<String, String> hashMap) throws Exception;

	String getBalance(HashMap<String, String> hashMap) throws Exception;
	
}
