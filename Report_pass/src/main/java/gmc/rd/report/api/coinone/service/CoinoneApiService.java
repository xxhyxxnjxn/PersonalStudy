package gmc.rd.report.api.coinone.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import gmc.rd.report.api.coinone.vo.CoinoneBalanceVo;
import gmc.rd.report.api.coinone.vo.CoinoneTransactionVo;

public interface CoinoneApiService {

	String getOrderBook(HashMap<String, String> map) throws Exception;

	JSONObject getTicker(HashMap<String, String> map) throws Exception;

	String getBalance(HashMap<String, String> hashmap)throws Exception;
	CoinoneBalanceVo getBalanceVo(HashMap<String, String> hashmap);

	String limitBuyOrder(HashMap<String, String> hashmap)throws Exception;
	//String coin, String price, String units, String connect, String secret
	String limitSellOrder(HashMap<String, String> hashmap)throws Exception;
	//String coin, String price, String units, String connect, String secret
	JSONObject getOrderDetail(HashMap<String, String> hashmap)throws Exception;
	
	JSONObject MyLimitOrders(HashMap<String, String> hashMap) throws Exception;
	
	JSONObject cancelOrder(HashMap<String, String> hashMap) throws Exception;

	JSONObject showcoinoneTransactionHistory(String coin, String connect, String secret) throws Exception;

	
	
	void updateStatus(HashMap<String, String> hashMap);

	JSONObject showcoinonecandle(String coin, String type) throws Exception;

	void insertCandleList(HashMap<String, Object> map);

	JSONObject candle(String coin);

	String MyCompletedOrders(HashMap<String, String> hashMap) throws Exception;
	List<CoinoneTransactionVo> MyCompletedOrders2(HashMap<String, String> hashMap) throws Exception;

}
