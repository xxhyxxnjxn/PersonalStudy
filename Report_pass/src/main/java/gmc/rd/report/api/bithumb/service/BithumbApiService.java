package gmc.rd.report.api.bithumb.service;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import gmc.rd.report.api.bithumb.vo.BithumbBalanceVo;
import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;

public interface BithumbApiService {

String getOrderBook(HashMap<String,String> hash);
	
	String getTicker(HashMap<String,String> hash);
	
	String getBalance(HashMap<String,String> hash);
	BithumbBalanceVo getBalanceVo(HashMap<String,String> hash);
	
	String limitBuyOrder(HashMap<String,String> hash);

	String limitSellOrder(HashMap<String,String> hash);
	
	String marketBuyOrder(HashMap<String,String> hash);
	
	String marketSellOrder(HashMap<String,String> hash);
	
	String cancelOrder(HashMap<String,String> hash);
	
	String getOrders(String apiKey,String secretKey,String orderCurrency);
	
	String getOrderDetail(HashMap<String,String> hash);

	String getOrderList(HashMap<String,String> hash);
	
	List<BithumbTransactionVo> getUserTransaction(String apiKey,String secretKey,String orderCurrency, String offSet);
	
	JsonNode getKakaoAccessToken(String code);
	
	JsonNode getKakaoUserInfo(String accessToken);
	
	JsonNode kakaoLogout(String accessToken);
	
	JsonNode getGoogleAccessToken(String code);
	
	JsonNode getGoogleUserInfo(String accessToken);

	String getCandleStick(String currency,String candleType);
	
	//String getTrades(String coin); // 내 체결내역
}
