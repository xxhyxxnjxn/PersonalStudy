package com.spring.coinoneh.api.service;

import java.util.HashMap;


import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.spring.coinoneh.api.service.Encryptor;
import com.spring.coinoneh.api.service.HTTPUtil;
import com.spring.coinoneh.api.service.CoinoneApiService;

@Service("coinoneApiService")
public class CoinoneApiServiceImpl implements CoinoneApiService{
	
	
	
	String api_url = "https://api.coinone.co.kr/";
	String orderbook = "orderbook?currency=";
	String ticker = "ticker?currency=";
	String trade = "trades?currency=";
	String PermissionlessApi = "https://tb.coinone.co.kr/api/v1/chart/olhc/";
	String site2 = "?site=coinone&type=1h";
	String sitea = "?site=";
	String candlehour = "&type=";
	//https://tb.coinone.co.kr/api/v1/chart/olhc/?site=coinone&type=1h
	//https://tb.coinone.co.kr/api/v1/chart/olhc/?site=coinone&type=1h
	@Override
	public String getOrderBook(HashMap<String, String> map) throws Exception{
		JSONObject jsonObject = HTTPUtil.getJSONfromGet(api_url+orderbook+map.get("currency"));
//        System.out.println("orderBook");
        //System.out.println(jsonObject);
		String result = jsonObject.toString();
        return result;
	}
	
	@Override
	public JSONObject getTicker(HashMap<String, String> map) throws Exception {
		JSONObject jsonObject = HTTPUtil.getJSONfromGet(api_url+ticker+map.get("currency"));
//        System.out.println("ticker");
//        System.out.println(jsonObject);
        return jsonObject;
	}
	
	@Override
	public String getBalance(HashMap<String, String> hashmap) throws Exception {
		
		
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashmap.get("apiKey");
		
       // System.out.println("getbalance : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

     
        String url = API_URL + "v2/account/balance/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashmap.get("secretKey").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject jsonObject = HTTPUtil.getJSONfromPost(url, map, payload);
        //System.out.println("getBalance");
        //System.out.println(result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        String result = jsonObject.toString();
        
        return result;
	}
	
	@Override
	public String limitBuyOrder(HashMap<String, String> hashmap) throws Exception {
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashmap.get("apiKey");
		
        System.out.println("limitBuy : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

        
        String url = API_URL + "v2/order/limit_buy/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);
        params.put("price", hashmap.get("price"));
        params.put("qty", hashmap.get("units"));
        params.put("currency", hashmap.get("currency"));

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashmap.get("secretKey").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject jsonObject = HTTPUtil.getJSONfromPost(url, map, payload);
        //System.out.println("limitBuy");
        //System.out.println(result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        String result = jsonObject.toString();
        
        return result;
	}
	
	@Override
	public String limitSellOrder(HashMap<String, String> hashmap)
			throws Exception {
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashmap.get("apiKey");
		
        System.out.println("limitSell : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

        
        String url = API_URL + "v2/order/limit_sell/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);
        params.put("price", hashmap.get("price"));
        params.put("qty", hashmap.get("units"));
        params.put("currency", hashmap.get("currency"));

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashmap.get("secretKey").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject jsonObject = HTTPUtil.getJSONfromPost(url, map, payload);
//        System.out.println("limitBuy");
//        System.out.println(result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        String result = jsonObject.toString();
        
        return result;
	}
	
	@Override
	public JSONObject getOrderDetail(HashMap<String, String> hashmap) throws Exception {
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashmap.get("apiKey");
		
        System.out.println("myOrderInformation : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

        
        String url = API_URL + "v2/order/order_info/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);
        params.put("order_id", hashmap.get("orderId"));
        params.put("currency", hashmap.get("currency"));

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashmap.get("secretKey").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject result = HTTPUtil.getJSONfromPost(url, map, payload);
        System.out.println("limitBuy");
        System.out.println(result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        
        return result;
	}
	
	
	@Override
	public JSONObject MyLimitOrders(HashMap<String, String> hashMap) throws Exception {
		// TODO Auto-generated method stub
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashMap.get("connect");
		
//        System.out.println("MyLimitOrders : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

        
        String url = API_URL + "v2/order/limit_orders/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);
        params.put("currency", hashMap.get("currency"));

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashMap.get("secret").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject result = HTTPUtil.getJSONfromPost(url, map, payload);
//        System.out.println("limitBuy");
//        System.out.println(result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        
        return result;
	}
	
	@Override
	public String MyCompletedOrders(HashMap<String, String> hashMap) throws Exception {
		// TODO Auto-generated method stub    v2/order/complete_orders/
		long nonce = (long) System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashMap.get("apiKey");
		
//        System.out.println("MyLimitOrders : "+nonce);
		
		apikey.put("nonce", String.valueOf(nonce));
		
		String url = API_URL + "v2/order/complete_orders/";
		JSONObject params = new JSONObject();
		params.put("nonce", nonce);
		params.put("access_token", accessToken);
		params.put("currency", hashMap.get("currency"));
		
		
		String payload = Base64.encodeBase64String(params.toString().getBytes());
		String signature = Encryptor.getHmacSha512(hashMap.get("secretKey").toUpperCase(), payload).toLowerCase();
		
		Map<String, String> map = new HashMap<>();
		map.put("content-type", "application/json");
		map.put("accept", "application/json");
		map.put("X-COINONE-PAYLOAD", payload);
		map.put("X-COINONE-SIGNATURE", signature);
		
		JSONObject result = HTTPUtil.getJSONfromPost(url, map, payload);
//        System.out.println("limitBuy");
//        System.out.println(result);
		//String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
		
		
		return result.toString();
	}
	
	
	
	
	
	
	
	@Override
	public JSONObject cancelOrder(HashMap<String, String> hashMap) throws Exception {
		// TODO Auto-generated method stub
		long nonce = (long)System.currentTimeMillis()*1000;
		String API_URL = "https://api.coinone.co.kr/";
		Map<String, String> apikey = new HashMap<>();
		
		String accessToken = hashMap.get("connect");
		
//        System.out.println("limitBuy : "+nonce);
        
        apikey.put("nonce", String.valueOf(nonce));

        
        String url = API_URL + "v2/order/cancel/";
        JSONObject params = new JSONObject();
        params.put("nonce", nonce);
        params.put("access_token", accessToken);
        params.put("order_id", hashMap.get("order_id") );
        params.put("price", hashMap.get("price"));
        params.put("qty", hashMap.get("qty"));
        params.put("is_ask", 1);
        params.put("currency", hashMap.get("currency"));

        
        String payload = Base64.encodeBase64String(params.toString().getBytes());
        String signature = Encryptor.getHmacSha512(hashMap.get("secret").toUpperCase(), payload).toLowerCase();

        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("accept", "application/json");
        map.put("X-COINONE-PAYLOAD", payload);
        map.put("X-COINONE-SIGNATURE", signature);

        JSONObject result = HTTPUtil.getJSONfromPost(url, map, payload);
//        System.out.println("limitBuy");
//        System.out.println("result"+result);
        //String strBalance = (String)((JSONObject) result.get(coin)).get("avail");
        
        
        return result;
	}
	
	
	@Override
	public JSONObject showcoinoneTransactionHistory(String coin, String connect, String secret) throws Exception {
		JSONObject jsonObject = HTTPUtil.getJSONfromGet(api_url+trade+coin);
//        System.out.println("trade");
//        System.out.println(jsonObject);
        return jsonObject;
	}
	//캔들
	@Override
	public JSONObject showcoinonecandle(String site, String type) throws Exception {
		
//			System.out.println("받은 코인종류"+site);  
//			System.out.println("////");
//			System.out.println(PermissionlessApi+sitea+site+candlehour+type);
		  //System.out.println(PermissionlessApi+site+chCoin+candlehour+type);
		  JSONObject jsonObject = HTTPUtil.getJSONfromGet("https://tb.coinone.co.kr/api/v1/chart/olhc/?site=coinone&type=1m");
//	      System.out.println("candle");
//	      System.out.println(jsonObject);
	      return jsonObject;
	}
	
	
	
	
	
		
	
	
	
	

	@Override
	public void updateStatus(HashMap<String, String> hashMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertCandleList(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject candle(String coin) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
