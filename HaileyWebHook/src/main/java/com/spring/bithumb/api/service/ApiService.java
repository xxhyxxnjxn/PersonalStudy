package com.spring.bithumb.api.service;

public interface ApiService {

	String placebuy(String coin, String price, String units, String apiKey, String secretKey);

	String placesell(String coin, String price, String units, String apiKey, String secretKey);
	
	String marketbuy(String coin,String units,String apiKey, String secretKey);
	
	String marketsell(String coin,String units,String apiKey, String secretKey);
	
	String showOrderDetail(String coin, String order_id, String apiKey, String secretKey);

	String showOrder(String coin,String apiKey, String secretKey);

	String showTicker(String coin);

	String showBalance(String coin, String apiKey, String secretKey);

	String cancel(String order_id, String type, String coin, String apiKey, String secretKey);
}
