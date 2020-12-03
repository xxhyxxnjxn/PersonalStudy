package com.spring.api.bithumb.service;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

public interface ApiService {

String getOrderBook(HashMap<String,String> hash);
	
	String getTicker(HashMap<String,String> hash);
	
	String getBalance(HashMap<String,String> hash);
	
	String limitBuyOrder(HashMap<String,String> hash);

	String limitSellOrder(HashMap<String,String> hash);
	
	String marketBuyOrder(HashMap<String,String> hash);
	
	String marketSellOrder(HashMap<String,String> hash);
	
	String cancelOrder(HashMap<String,String> hash);
	
	String getOrderDetail(HashMap<String,String> hash);

	String getOrderList(HashMap<String,String> hash);
	
	JsonNode getKakaoAccessToken(String code);
	
	JsonNode getKakaoUserInfo(String accessToken);
	
	JsonNode kakaoLogout(String accessToken);
	
	JsonNode getGoogleAccessToken(String code);
	
	JsonNode getGoogleUserInfo(String accessToken);
	
	//String getTrades(String coin); // 내 체결내역
}
