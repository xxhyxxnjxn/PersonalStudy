package com.bitforex.api.service.impl;

import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitforex.api.client.APIClient2;
import com.bitforex.api.client.HmacSHA256Base64Utils;
import com.bitforex.api.service.BitforexApiService;

@Service("BitforexApiService")
public class BitforexApiServiceImpl implements BitforexApiService{
	
	//ex)
	//symbol = "coin-usdt-btc"
	//currency = "btc"
	
	String ticker = "/api/v1/market/ticker";
	String depth="/api/v1/market/depth";
	String balance = "/api/v1/fund/mainAccount";
	String symbol = "/api/v1/market/symbols";
	String order = "/api/v1/trade/placeOrder";
	
	@Override
	public String getTicker(String symbol) {
		APIClient2 client = new APIClient2();
		HashMap <String, String> map = new HashMap();
		map.put("symbol",symbol);
		String result = client.get(ticker,map);
		return result;
	}
	@Override
	public String getDepth(String symbol) {
		APIClient2 client = new APIClient2();
		HashMap <String, String> map = new HashMap();
		map.put("symbol",symbol);
		
		String result = client.get(depth,map);
		return result;
	}
	@Override
	public String getSymbol() {
		APIClient2 client = new APIClient2();
		HashMap <String, String> map = new HashMap();
		
		
		String result = client.getSymbol(symbol);
		return result;
		
	}
	@Override
	public void getBalance(String currency) { // balance는 화폐검색할때 약어롤 써야해서 currency임
		APIClient2 client = new APIClient2();
		client.APISet("your-api-Key","your-secret-key");
		
		HashMap <String, String> map = new HashMap();
		map.put("currency",currency);
		client.post(balance,map);
		
		
	}
	@Override
	public void tradeOrder(String currency) {
		APIClient2 client = new APIClient2();
		client.APISet("12aedb5c4b1ed733a722cee8af95c652","53a00f641963514ad6b8b08387ca1405");
		
		HashMap <String, String> map = new HashMap();
		map.put("symbol",currency);
		map.put("price", "2.6936");
		map.put("amount","0.1" );
		map.put("tradeType", "2");
		
		String result = client.post(order,map);
		System.out.println(result);
	}
	
}
