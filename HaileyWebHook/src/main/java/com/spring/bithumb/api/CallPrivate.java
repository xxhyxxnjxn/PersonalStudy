package com.spring.bithumb.api;

import java.util.HashMap;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.java_cup.internal.runtime.Scanner;

public class CallPrivate {
	String account = "/info/account";
	String balance = "/info/balance"; 
	String wallet_address ="/info/wallet_address";
	String ticker = "/info/ticker";
	String orders = "/info/orders";
	String order_detail = "info/order_detail";
	String cancel = "/trade/cancel";
	String place = "/trade/place";
	String markBuy = "/trade/market_buy";
	String markSell = "/trade/market_sell";
	
	Api_Client api = new Api_Client("822c955bb51f90d80dcb4efdf467f766",
			"a8222f81d0076f70c309040cc7f1d81b");
	
	HashMap<String, String> rgParams = new HashMap<String, String>();
	HashMap<String, String> rgParams2 = new HashMap<String, String>();
	String AccountCall(String coin) {
		
		rgParams.put("order_currency", coin);
		String result = api.callApi(account, rgParams);
		
		return result;
		
	}
	String BalanceCall(String coin) {
		rgParams.put("currency", coin);
		String result = api.callApi(balance, rgParams);
		return result;
	}
	String Wallet_address(String coin) {
		rgParams.put("currency", coin);
		String result = api.callApi(wallet_address, rgParams);
		return result;
	}
	String Ticker(String coin) {
		rgParams.put("order_currency", coin);
		String result = api.callApi(ticker, rgParams);
		return result;
	}
	String Orders(String coin) {
//		rgParams.put("order_currency", coin);
//		String result = api.callApi(orders, rgParams);
//		
//		JSONParser parser = new JSONParser();
//		try {
//			Object obj = parser.parse( result );
//			System.out.println("메소?��?��?��?��");
//			System.out.println(obj);
//			JSONObject jsonObj = (JSONObject) obj;
//			System.out.println(jsonObj.get("data"));
//			System.out.println(" ");
//			JSONArray jArray = (JSONArray)jsonObj.get("data");
//			for (int i = 0; i < jArray.size(); i++) {
//				jArray.get(i);
//				JSONObject jsonObj2 = (JSONObject) jArray.get(i);
//				System.out.println(jsonObj2);
//				System.out.println("코인 ?���? : "+jsonObj2.get("order_currency"));
//				System.out.println("주문번호 : " + jsonObj2.get("order_id"));
//				System.out.println("�?�? : "+jsonObj2.get("price"));
//				System.out.println(" ");
//			}
//				
//			System.out.println(" ");
//			//System.out.println(jsonObj2);
//			System.out.println("메소?�� ?��?��?�� ?��");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		rgParams.put("order_currency", coin);
		String result = api.callApi(orders, rgParams);
		JSONParser parser = new JSONParser();
		HashMap<String, String> hash;
		try {
			Object obj = parser.parse( result );
			System.out.println("메소?��?��?��?��");
			JSONObject jsonObj = (JSONObject) obj;
			System.out.println(" ");
			JSONArray jArray = (JSONArray)jsonObj.get("data");
			for (int i = 0; i < jArray.size(); i++) {
				jArray.get(i);
				JSONObject jsonObj2 = (JSONObject) jArray.get(i);
				System.out.println(i);
				System.out.println("코인 ?���? : "+jsonObj2.get("order_currency"));
				System.out.println("주문번호 : " + jsonObj2.get("order_id"));
				System.out.println("�?�? : "+ jsonObj2.get("price"));
				System.out.println(" ");
				
			}
			System.out.println("메소?�� ?��?��?�� ?��");	
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	String Order_Detail(String coin) {
		rgParams.put("order_currency", coin);
		String result = api.callApi(order_detail, rgParams);
		
		return result;
	}
	String Cencel(String coin, int cancelnum) {
		
		rgParams.put("order_currency", coin);
		String result = api.callApi(orders, rgParams);
		JSONParser parser = new JSONParser();
		HashMap<String, String> hash;
		try {
			Object obj = parser.parse( result );
			System.out.println("메소?��?��?��?��");
			JSONObject jsonObj = (JSONObject) obj;
			System.out.println(" ");
			JSONArray jArray = (JSONArray)jsonObj.get("data");
			
			JSONObject jsonObj2 = (JSONObject) jArray.get(cancelnum);
			rgParams2.put("order_currency", coin);
			rgParams2.put("type", "bid");
			rgParams2.put("order_id", (String)jsonObj2.get("order_id"));
			String result2 = api.callApi(cancel, rgParams2);
			return result2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	String Place(String coin, String type, String units, String price) {
		rgParams.put("order_currency", coin);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("type", type);
		rgParams.put("units", units);
		rgParams.put("price", price);
		String result = api.callApi(place, rgParams);
		return result;
		
	}
	String MarketBuy(String coin, String units) {
		rgParams.put("units", units);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("order_currency", coin);
		String result = api.callApi(markBuy, rgParams);
		System.out.println("complete marketbuy");
		
		return result;
	}
	String MarketSell(String coin, String units) {
		rgParams.put("units", units);
		rgParams.put("payment_currency", "KRW");
		rgParams.put("order_currency", coin);
		String result = api.callApi(markSell, rgParams);
		System.out.println("complete marketbuy");
		
		return result;
	}
	
}
