package com.spring.coinoneh.api.service;

import java.util.HashMap;

import org.json.JSONObject;

public interface CoinoneApiService {

	JSONObject showCoinoneOrderBook(String coin, String connect, String secret) throws Exception;

	JSONObject showCoinoneTicker(String coin, String connect, String secret) throws Exception;

	JSONObject showCoinoneBalance(String coin, String connect, String secret)throws Exception;

	JSONObject limitBuy(String coin, String price, String units, String connect, String secret)throws Exception;

	JSONObject myOrderInformation(String coin, String order_id, String connect, String secret)throws Exception;

	JSONObject limitSell(String coin, String price, String units, String connect, String secret)throws Exception;

	JSONObject CancelOrder(HashMap<String, String> hashMap) throws Exception;

	JSONObject showcoinoneTransactionHistory(String coin, String connect, String secret) throws Exception;

	JSONObject MyLimitOrders(HashMap<String, String> hashMap) throws Exception;
	

}
