package com.spring.api.bithumb;
import java.util.HashMap;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import jdbc.TickerInputDB;



public class CallPublic {
	String callticker = "/public/ticker/";
	String callorder = "/public/orderbook/"; 
	String calltransaction ="/public/transaction_history/";
	String assetsstatus = "/public/assetsstatus/";
	String btci = "/public/btci/";
	
	
	Api_Client api = new Api_Client("778a166eb769dba47e4ff38c363dd772",
			"2bc5bd6a14b5e56743b657e951127a79");
	
	HashMap<String, String> rgParams = new HashMap<String, String>();
	
	public String CallMethod(int number, String inputparams, String inputparams2) {
		
		if(number == 1) {
			rgParams.put("order_currency", inputparams);
			rgParams.put("payment_currency", inputparams2);
			
			String result = api.callApi(callticker+inputparams+"_"+inputparams2, rgParams);
			System.out.println("+++++++++++++++");
			System.out.println(result);
			System.out.println("+++++++++++++++");
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse( result );
				System.out.println(obj);
				JSONObject jsonObj = (JSONObject) obj;
				jsonObj.get("data");
				System.out.println(jsonObj.get("data"));
				JSONObject jsonObj2 = (JSONObject) jsonObj.get("data");
				
				
				String opening_price = (String) jsonObj2.get("opening_price");
				String closing_price = (String) jsonObj2.get("closing_price");
				String min_price = (String) jsonObj2.get("min_price");
				String max_price = (String) jsonObj2.get("max_price");
				String units_traded = (String) jsonObj2.get("units_traded");
				String acc_trade_value = (String) jsonObj2.get("acc_trade_value");
				String prev_closing_price = (String) jsonObj2.get("prev_closing_price");
				String units_traded_24H = (String) jsonObj2.get("units_traded_24H");
				String acc_trade_value_24H = (String) jsonObj2.get("acc_trade_value_24H");
				String fluctate_24H = (String) jsonObj2.get("fluctate_24H");
				String fluctate_rate_24H = (String) jsonObj2.get("fluctate_rate_24H");
				String date = (String) jsonObj2.get("date");
				
					
				//TickerInputDB tickerInputDB =  new TickerInputDB();
//				.TickerInput(opening_price,closing_price,min_price,max_price
//						,units_traded,acc_trade_value,prev_closing_price,units_traded_24H
//						,acc_trade_value_24H,fluctate_24H,fluctate_rate_24H,date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("+++++++++++++++");
			return result;
		}
		else if(number == 2) {
			rgParams.put("order_currency", inputparams);
			rgParams.put("payment_currency", inputparams2);
			String result = api.callApi(callorder+inputparams+"_"+inputparams2, rgParams);
			System.out.println(result);
			
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse( result );
				System.out.println(obj);
				JSONObject jsonObj = (JSONObject) obj;
				jsonObj.get("data");
				System.out.println("******");
				System.out.println(jsonObj.get("data"));
				JSONObject jsonObj2 = (JSONObject) jsonObj.get("data");
				System.out.println("bids : " + jsonObj2.get("bids"));
				
				JSONArray jArray = (JSONArray)jsonObj2.get("bids");
				for (int i = 0; i < jArray.size(); i++) {
					jArray.get(i);
					System.out.println(jArray.get(i));
					
				}
				//json �? 추출
				JSONObject jsonObj3 = (JSONObject) jArray.get(1);
				System.out.println("***********");
				System.out.println(jsonObj3);
				System.out.println("***********");
				System.out.println(jArray);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		else if(number == 3) {
			rgParams.put("order_currency", inputparams);
			rgParams.put("payment_currency", inputparams2);
			String result = api.callApi(calltransaction+inputparams+"_"+inputparams2, rgParams);
			System.out.println(result);
			
			return result;
		}
		else if(number == 4) {
			rgParams.put("order_currency", inputparams);
			rgParams.put("payment_currency", inputparams2);
			String result = api.callApi(assetsstatus, rgParams);
			System.out.println(result);
		}
		else if(number == 5) {
			rgParams.put("order_currency", inputparams);
			rgParams.put("payment_currency", inputparams2);
			String result = api.callApi(btci, rgParams);
			System.out.println(result);
		}
		return null;
		
	}
	
	
	
}
