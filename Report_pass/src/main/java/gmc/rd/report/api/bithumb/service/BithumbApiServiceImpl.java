package gmc.rd.report.api.bithumb.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import gmc.rd.report.api.bithumb.dao.Api_Client;
import gmc.rd.report.api.bithumb.vo.BithumbBalanceVo;
import gmc.rd.report.api.bithumb.vo.BithumbDataVo;
import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceVo;
import gmc.rd.report.dto.ReportDtoMapper;
import gmc.rd.report.entity.Report;
import gmc.rd.report.repository.ReportRepository;

@Service("candleService")
public class BithumbApiServiceImpl implements BithumbApiService{

	String candlestick = "/public/candlestick/";
	String account = "/info/account";
	String balance = "/info/balance"; 
	String wallet_address ="/info/wallet_address";
	String ticker = "/public/ticker/";
	String orderBook = "/public/orderbook";
	String transactionHistory="/public/transaction_history";
	String orders = "/info/orders";
	String order_detail = "/info/order_detail";
	String cancel = "/trade/cancel";
	String place = "/trade/place";
	String marketBuy = "/trade/market_buy";
	String marketSell = "/trade/market_sell";
	String transactions = "/info/user_transactions";
	
	HashMap<String, String> rgParams = new HashMap<String, String>();

	
	@Override
	public String getOrderBook(HashMap<String,String> hash) {
		Api_Client api = new Api_Client("","");
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		//rgParams.put("count", "5");
		
		String result="";
		
		try {
			result = api.callApi2("/public/orderbook/"+hash.get("currency")+"_KRW", rgParams);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String getTicker(HashMap<String,String> hash) {
		Api_Client api = new Api_Client("","");
		
		String result = api.callApi2_Post("/public/ticker/"+hash.get("currency")+"_KRW", rgParams);
		
		return result;
	}
	
	@Override
	public String getBalance(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		rgParams.put("currency", hash.get("currency"));
		
		String result = api.callApi(balance, rgParams);

		System.out.println(result);
		return result;
	}
	
	@Override
	public BithumbBalanceVo getBalanceVo(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		String result = api.callApi(balance, rgParams);
		Gson gson = new Gson();
		BithumbBalanceVo bithumbBalanceVo = gson.fromJson(result, BithumbBalanceVo.class);

		return bithumbBalanceVo;
	}
	
	@Override
	public String limitBuyOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		rgParams.put("price", hash.get("price"));
		rgParams.put("type", "bid");
		
		String result = api.callApi(place, rgParams);
		
		return result;
	}
	@Override
	public String limitSellOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		rgParams.put("price", hash.get("price"));
		rgParams.put("type", "ask");
		String result = api.callApi(place, rgParams);
		
		return result;
	}
	
	@Override
	public String marketBuyOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		
		String result = api.callApi(marketBuy, rgParams);
		return result;
	}
	@Override
	public String marketSellOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("units", hash.get("units"));
		
		String result = api.callApi(marketSell, rgParams);
		
		return result;
	}
	@Override
	public String cancelOrder(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		rgParams.put("order_id", hash.get("orderId"));
		rgParams.put("type", hash.get("type"));
		
		String result = api.callApi(cancel, rgParams);
		
		return result;
	}
	
	@Override
	public String getOrderDetail(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_id", hash.get("orderId"));
		rgParams.put("order_currency", hash.get("currency"));
		rgParams.put("payment_currency", "KRW");
		
		String result = api.callApi(order_detail, rgParams);
		
		return result;
	}
	@Override
	public String getOrderList(HashMap<String,String> hash) {
		Api_Client api = new Api_Client(hash.get("apiKey"),hash.get("secretKey"));
		
		rgParams.put("order_currency", hash.get("currency"));
		
		String result = api.callApi(transactions, rgParams);
		
		return result;
	}
	
	@Override
	public List<BithumbTransactionVo> getUserTransaction(String apiKey,String secretKey,String orderCurrency) {
		Api_Client api = new Api_Client(apiKey,secretKey);
		
		
		
		rgParams.put("offset", "0");
		rgParams.put("count", "50");
		rgParams.put("searchGb", "0");
		rgParams.put("order_currency", orderCurrency);
		rgParams.put("payment_currency", "KRW");
		
		List<BithumbTransactionVo> transaction = null;
		
		String result = api.callApi(transactions, rgParams);

		
		try {

			ObjectMapper mapper = new ObjectMapper();
			JsonNode treeNode = mapper.readTree(result);
			if(treeNode.get("data").toString().length() > 2) {
				System.out.println(result);
				
				try {
				
				
					Gson gson = new Gson();
					BithumbDataVo bithumbTransactionVo = gson.fromJson(result, BithumbDataVo.class);
					transaction = bithumbTransactionVo.getData();
				
				}catch(NullPointerException e) {
		            e.printStackTrace();
		        }

			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return transaction;
	}
	
	@Override
	public JsonNode getKakaoAccessToken(String code) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getKakaoAccessToken(code);
		return result;
	}
	
	@Override
	public JsonNode getKakaoUserInfo(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getKakaoUserInfo(accessToken);
		return result;
	}
	@Override
	public JsonNode kakaoLogout(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.kakaoLogout(accessToken);
		return result;
	}
	@Override
	public JsonNode getGoogleAccessToken(String code) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getGoogleAccessToken(code);
		return result;
	}
	
	@Override
	public JsonNode getGoogleUserInfo(String accessToken) {
		Api_Client api = new Api_Client("","");
		
		JsonNode result = api.getGoogleUserInfo(accessToken);
		return result;
	}
	
}
